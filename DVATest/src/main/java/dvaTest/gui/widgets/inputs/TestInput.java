package dvaTest.gui.widgets.inputs;

import dvaTest.testCore.TestController;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;

public abstract class TestInput extends Pane {

    protected TestController testController;

    public TestInput() {
    }

    public void setTestController(TestController testController) {
        this.testController = testController;
    }

    public abstract void keyPressed(KeyEvent keyEvent);
}
