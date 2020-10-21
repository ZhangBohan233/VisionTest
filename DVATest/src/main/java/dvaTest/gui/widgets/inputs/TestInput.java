package dvaTest.gui.widgets.inputs;

import dvaTest.testCore.TestController;
import javafx.scene.layout.Pane;

public abstract class TestInput extends Pane {

    protected TestController testController;

    public TestInput() {
    }

    public void setTestController(TestController testController) {
        this.testController = testController;
    }
}
