package dvaTest.testCore;

import common.EventLogger;
import common.Signals;
import common.data.AutoSavers;
import dvaTest.connection.ClientManager;
import dvaTest.gui.TestView;
import dvaTest.testCore.tests.Test;
import dvaTest.testCore.tests.TestUnit;

import java.io.IOException;
import java.util.*;

public class TestController implements ITestController {
    private final int maxCount = AutoSavers.getPrefSaver().getInt("eachLevel", 5);

    private final TestPref testPref;
    private final TestView testView;
    private final Test test;
    private final ResultRecord.UnitList[] testResults;
    private final LevelAllocator levelAllocator;
    private Date testStartTime;
    private Timer baseTimer;
    private TestUnit curTrueUnit;
    private String userInputName;

    public TestController(TestPref testPref, TestView testView) {
        this.testPref = testPref;
        this.testView = testView;

        test = testPref.getTestType().getTest();

        int totalLevelCount = test.visionLevelCount();
        testResults = new ResultRecord.UnitList[totalLevelCount];
        for (int i = 0; i < totalLevelCount; i++) {
            testResults[i] = new ResultRecord.UnitList();
        }
        levelAllocator = new LevelAllocator(test.visionLevelCount() / 2);
    }

    /**
     * 开始测试
     */
    public void start() {
        testStartTime = new Date();
        baseTimer = new Timer();
        baseTimer.schedule(new TestTask(), 0, testPref.getIntervalMills() + testPref.getHidingMills());
    }

    /**
     * 由测试结束触发的正常退出
     */
    public void normalStop() {
        baseTimer.cancel();
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

    @Override
    public void interrupt() {
        baseTimer.cancel();
    }

    public void userInput(String name, String buttonText) {
        userInputName = name;
        testView.updateInput(buttonText);
    }

    public TestView getTestView() {
        return testView;
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

    private void finishTest() {
        try {
            ClientManager.getCurrentClient().sendMessage(Signals.STOP_TEST);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ResultRecord resultRecord = new ResultRecord(testResults, testPref, testStartTime);

        testView.showResult(resultRecord);
        testView.closeWindow();
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
