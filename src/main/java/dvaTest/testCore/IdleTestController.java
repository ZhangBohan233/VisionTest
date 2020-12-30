package dvaTest.testCore;

import dvaTest.gui.TestPrepView;

/**
 * 一个没有实际作用，只用于占位的控制器
 */
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
    public void interruptByScreen() {
    }
}
