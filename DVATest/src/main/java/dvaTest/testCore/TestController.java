package dvaTest.testCore;

import common.EventLogger;
import common.Signals;
import dvaTest.connection.ClientManager;
import dvaTest.gui.TestView;
import dvaTest.testCore.tests.Test;
import dvaTest.testCore.tests.TestUnit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class TestController implements ITestController {

    /**
     * 切换图片的等待时间
     * 在此期间将不会显示任何图片
     */
    public static final long BLANK_WAIT_TIME = 1000;

    private final TestPref testPref;
    private final TestView testView;
    private final Test test;
    private final TestLevelAllocator levelAllocator;
//    private boolean interrupted = false;
    private Timer baseTimer;

    private TestUnit curTrueUnit;
    private String userInputName;

    private final List<TestResultUnit> testResultUnits = new ArrayList<>();

    public TestController(TestPref testPref, TestView testView) {
        this.testPref = testPref;
        this.testView = testView;

        test = testPref.getTestType().getTest();

        this.levelAllocator =
                new TestLevelAllocator(test.visionLevelCount(), test.visionLevelCount() / 2);
    }

    public void start() {
        baseTimer = new Timer();
        baseTimer.schedule(new TestTask(), 0, testPref.getIntervalMills() + testPref.getHidingMills());
    }

    public void normalStop() {
//        interrupted = true;
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
        TestResultUnit tru = new TestResultUnit(curTrueUnit, userInputName);
        testResultUnits.add(tru);

        if (tru.isCorrect()) {
            // 正确的结果
            levelAllocator.correctResult();
        } else {
            levelAllocator.incorrectResult();
        }
    }

    private void finishTest() {
//        System.out.println(testResultUnits);

        try {
            ClientManager.getCurrentClient().sendMessage(Signals.STOP_TEST);
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<ResultRecord.RecordUnit> resultRecords = new ArrayList<>();
        for (TestResultUnit tru : testResultUnits) {
            resultRecords.add(ResultRecord.RecordUnit.fromTest(tru));
        }

        ResultRecord resultRecord = new ResultRecord(resultRecords, testPref);

        testView.showResult(resultRecord);
        testView.closeWindow();
    }

    public static class TestResultUnit {
        private final TestUnit testUnit;
        private final String userInput;  // "" if no input
        private final boolean correct;

        TestResultUnit(TestUnit testUnit, String userInput) {
            this.testUnit = testUnit;
            this.userInput = userInput;
            this.correct = testUnit.getTestImage().getName().equals(userInput);
        }

        public String getUserInput() {
            return userInput;
        }

        public TestUnit getTestUnit() {
            return testUnit;
        }

        public boolean isCorrect() {
            return correct;
        }

        @Override
        public String toString() {
            return String.format("Given: %s, input: %s", testUnit, userInput);
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
                curTrueUnit = test.generate(levelAllocator.next(), testPref);
                System.out.println("generated: " + curTrueUnit);

                try {
                    ClientManager.getCurrentClient().sendTestUnit(curTrueUnit);
                } catch (IOException e) {
                    cancel();
                    EventLogger.log(e);
                    throw new RuntimeException(e);
                }

                testView.updateGui(curTrueUnit);
            } else {
                finishTest();
                cancel();
            }
        }

        @Override
        public boolean cancel() {
            boolean res = super.cancel();
            System.out.println("Cancelled " + res);
            return res;
        }
    }
}
