package dvaTest.testCore;

import common.EventLogger;
import common.Signals;
import dvaTest.connection.ClientManager;
import dvaTest.gui.TestView;
import dvaTest.testCore.tests.CTest;
import dvaTest.testCore.tests.Test;
import dvaTest.testCore.tests.TestUnit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class TestController {

    private final TestPref testPref;
    private final TestView testView;
    private final Test test;
    private final TestLevelAllocator levelAllocator;
    private boolean interrupted = false;

    private TestUnit curTrueUnit;
    private String userInputName;

    private List<TestResultUnit> testResultUnits = new ArrayList<>();

    public TestController(TestPref testPref, TestView testView) {
        this.testPref = testPref;
        this.testView = testView;

        if (testPref.getTestType() == TestType.SNELLEN_CHART) {
            test = null;
        } else if (testPref.getTestType() == TestType.C_CHART) {
            test = new CTest();
        } else if (testPref.getTestType() == TestType.E_CHART) {
            test = null;
        } else {
            throw new RuntimeException("Unexpected test type. ");
        }

        this.levelAllocator = new TestLevelAllocator(test.visionLevelCount());
    }

    public void start() {

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                if (interrupted) {
                    cancel();
                    return;
                }

                // 处理上一次测试
                if (curTrueUnit != null) {
                    proceedOne();
                }

                if (levelAllocator.hasNext()) {
                    curTrueUnit = test.generate(levelAllocator.nextLevel());

                    try {
                        ClientManager.getCurrentClient().sendTestUnit(curTrueUnit);
                    } catch (IOException e) {
                        cancel();
                        EventLogger.log(e);
                        throw new RuntimeException(e);
                    }
                } else {
                    finishTest();
                    cancel();
                }
            }
        }, 0, testPref.getFrameTimeMills());
    }

    public void stop() {
        interrupted = true;
        try {
            ClientManager.getCurrentClient().sendMessage(Signals.STOP_TEST);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void userInput(String name) {
        userInputName = name;
    }

    private void proceedOne() {
        testResultUnits.add(new TestResultUnit(curTrueUnit, userInputName));

        if (curTrueUnit.getTestItem().getName().equals(userInputName)) {
            // 正确的结果
            levelAllocator.correctResult();
        } else {
            levelAllocator.incorrectResult();
        }
    }

    private void finishTest() {

    }

    private static class TestResultUnit {
        private final TestUnit testUnit;
        private final String userInput;

        private TestResultUnit(TestUnit testUnit, String userInput) {
            this.testUnit = testUnit;
            this.userInput = userInput;
        }
    }
}
