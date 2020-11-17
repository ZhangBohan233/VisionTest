package dvaTest.testCore;

import dvaTest.gui.TestPrepView;

public class IdleTestController implements ITestController {

    private final TestPrepView testPrepView;

    public IdleTestController(TestPrepView testPrepView) {
        this.testPrepView = testPrepView;
    }

    @Override
    public void closeTestView() {
        testPrepView.closeWindow();
    }

    @Override
    public void interrupt() {

    }
}
