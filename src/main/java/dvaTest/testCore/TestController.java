package dvaTest.testCore;

import common.EventLogger;
import common.Signals;
import common.data.AutoSavers;
import dvaTest.connection.ClientManager;
import dvaTest.gui.TestView;
import dvaTest.testCore.tests.Test;
import dvaTest.testCore.tests.TestUnit;
import javafx.application.Platform;

import java.io.IOException;
import java.util.*;

public class TestController implements ITestController {
    private final int maxCount = AutoSavers.getPrefSaver().getInt("eachLevel", 5);

    private final TestPref testPref;
    private TestView testView;
    private final Test test;

    private final int totalLevelCount;
    private final Queue<EyeSide> queue = new ArrayDeque<>();
    private final Map<EyeSide, ResultRecord.UnitList[]> resultMap = new TreeMap<>();
    private Date testStartTime;
    private SideTestController currentController;

    public TestController(TestPref testPref) {
        this.testPref = testPref;

        test = testPref.getTestType().getTest();

        if (testPref.isLeftEye()) queue.add(EyeSide.LEFT);
        if (testPref.isRightEye()) queue.add(EyeSide.RIGHT);
        if (testPref.isBothEyes()) queue.add(EyeSide.BOTH);

        if (queue.isEmpty()) throw new RuntimeException("Unexpected error, no test has been selected.");

        totalLevelCount = test.visionLevelCount();
    }

    public void setTestView(TestView testView) {
        this.testView = testView;
    }

    /**
     * @return 返回下一个要测试的眼侧，但不进行真正的测试。
     */
    public EyeSide getNextSide() {
        return queue.peek();
    }

    /**
     * 由小测试结束触发的正常退出
     */
    private void finishOneSideTest() {
        resultMap.put(currentController.side, currentController.testResults);
        if (!queue.isEmpty()) {
            EyeSide peek = queue.peek();
            testView.nextSideTest(peek);
            try {
                ClientManager.getCurrentClient().sendMessage(peek.toBytes());
            } catch (IOException e) {
                e.printStackTrace();
                EventLogger.log(e);
            }
        } else {
            finishTotalTest();
        }
    }

    private void finishTotalTest() {
        ResultRecord resultRecord = new ResultRecord(resultMap, testPref, testStartTime);
        testView.showResult(resultRecord);
        testView.closeWindow();

        try {
            ClientManager.getCurrentClient().sendMessage(Signals.STOP_TEST);
        } catch (IOException e) {
            e.printStackTrace();
            EventLogger.log(e);
        }
    }

    /**
     * 开始下一步测试
     */
    public void startPartialTest() {
        if (testStartTime == null) testStartTime = new Date();

        EyeSide eyeSide = queue.poll();
        assert eyeSide != null;
        Platform.runLater(() -> testView.setEyeLabel(eyeSide));

        currentController = new SideTestController(eyeSide);
        currentController.start();
    }

    /**
     * 由用户主动停止测试、关闭窗口造成的停止
     */
    public void stopByUser() {
        if (currentController != null) currentController.interrupt();
        try {
            ClientManager.getCurrentClient().sendMessage(Signals.STOP_TEST);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void closeTestView() {
        getTestView().closeWindow();
    }

    /**
     * 处理由屏幕端发来的中止测试信号
     */
    @Override
    public void interruptByScreen() {
        currentController.interrupt();
    }

    public void userInput(String name, String buttonText) {
        currentController.userInputName = name;
        testView.updateInput(buttonText);
    }

    public TestView getTestView() {
        return testView;
    }

    /**
     * 管理一侧眼睛视力测试的控制器
     */
    private class SideTestController {
        private final ResultRecord.UnitList[] testResults;
        private final LevelAllocator levelAllocator;
        private final EyeSide side;
        private Timer baseTimer;
        private TestUnit curTrueUnit;
        private String userInputName;

        SideTestController(EyeSide side) {
            this.side = side;

            testResults = new ResultRecord.UnitList[totalLevelCount];
            for (int i = 0; i < totalLevelCount; i++) {
                testResults[i] = new ResultRecord.UnitList();
            }
            levelAllocator = new LevelAllocator(totalLevelCount / 2);
        }

        private void start() {
            baseTimer = new Timer();
            baseTimer.schedule(new TestTask(), 0, testPref.getIntervalMills() + testPref.getHidingMills());
        }

        private void proceedOne() {
            ResultRecord.RecordUnit tru = new ResultRecord.RecordUnit(
                    curTrueUnit.getVisionLevel(),
                    curTrueUnit.getTestImage().getName(),
                    userInputName,
                    curTrueUnit.getTestImage().getName().equals(userInputName));
            testResults[levelAllocator.getCurrentIndex()].add(tru);
            levelAllocator.generateNext(tru.isCorrect());
        }

        private void interrupt() {
            baseTimer.cancel();
        }

        private boolean levelComplete(int levelIndex) {
            return testResults[levelIndex].size() == maxCount;
        }

        /**
         * @param levelIndex 等级index
         * @return 如果该等级已完成且正确率高于50%则返回{@code true}
         */
        private boolean levelSuccess(int levelIndex) {
            if (levelComplete(levelIndex)) {
                return testResults[levelIndex].correctCount() > maxCount / 2;  // 不用转换为double
            }
            return false;
        }

        private void finishTest() {
            finishOneSideTest();
        }

        private class LevelAllocator {

            private boolean neverCorr = true;
            private boolean neverFail = true;

            /**
             * 状态记录器。若为-1则结束
             */
            private int nextIndex;

            LevelAllocator(int initLevel) {
                nextIndex = initLevel;
            }

            /**
             * 产生下一个状态
             *
             * @param currIsCorrect 当前结果是否正确
             */
            void generateNext(boolean currIsCorrect) {
                int totalLevels = test.visionLevelCount();

                if (levelComplete(nextIndex)) {  // 该行已填满
                    if (levelSuccess(nextIndex)) {  // 该行正确率过半
                        nextIndex++;
                        if (nextIndex == totalLevels ||      // 达到最高等级
                                levelComplete(nextIndex)) {  // 下一级已完成
                            nextIndex = -1;  // 中止测试
                            return;
                        }
                    } else {
                        nextIndex--;
                        if (nextIndex < 0 ||                 // 达到最低级
                                levelComplete(nextIndex)) {  // 上一级已完成
                            nextIndex = -1;  // 中止测试
                            return;
                        }
                    }
                }
                // 未填满则状态不变

                if (currIsCorrect) {
                    neverCorr = false;

                    if (neverFail) {
                        // 从未错误，直接跳级
                        nextIndex = (totalLevels - nextIndex) / 2 + nextIndex;
                    }
                } else {
                    neverFail = false;

                    if (neverCorr) {
                        // 从未正确，直接跳级
                        nextIndex /= 2;
                    }
                }
            }

            boolean hasNext() {
                return nextIndex >= 0;
            }

            int next() {
                return nextIndex;
            }

            int getCurrentIndex() {
                return nextIndex;
            }
        }

        class TestTask extends TimerTask {
            @Override
            public void run() {
                // 处理上一次测试
                if (curTrueUnit != null) {
                    proceedOne();
                }

                if (levelAllocator.hasNext()) {
                    userInput("", "");  // 重置为无输入
                    int currentLevelIndex = levelAllocator.next();
                    curTrueUnit = test.generate(currentLevelIndex, testPref);
                    System.out.println("generated: " + curTrueUnit);

                    try {
                        ClientManager.getCurrentClient().sendTestUnit(curTrueUnit);
                    } catch (IOException e) {
                        baseTimer.cancel();
                        EventLogger.log(e);
                        throw new RuntimeException(e);
                    }

                    testView.updateGui(curTrueUnit);
                } else {
                    finishTest();
                    baseTimer.cancel();
                }
            }
        }
    }
}
