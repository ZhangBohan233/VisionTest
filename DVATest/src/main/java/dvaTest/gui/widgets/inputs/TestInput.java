package dvaTest.gui.widgets.inputs;

import dvaTest.gui.TestView;
import dvaTest.testCore.TestController;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class TestInput extends Pane {

    protected TestController testController;

    public TestInput() {
    }

    public void setTestController(TestController testController) {
        this.testController = testController;
    }
}
