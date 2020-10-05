package dvaTest.testCore;

import common.EventLogger;
import common.Signals;
import dvaTest.connection.ClientManager;
import dvaTest.gui.TestView;
import dvaTest.testCore.tests.CTest;
import dvaTest.testCore.tests.Test;
import dvaTest.testCore.tests.TestUnit;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class TestController {

    private final TestPref testPref;
    private final TestView testView;
    private final Test test;
    private boolean interrupted = false;

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

                TestUnit testUnit = test.generateNext();
                try {
                    ClientManager.getCurrentClient().sendTestUnit(testUnit);
                } catch (IOException e) {
                    cancel();
                    EventLogger.log(e);
                    throw new RuntimeException(e);
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
}
