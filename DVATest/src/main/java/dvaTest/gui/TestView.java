package dvaTest.gui;

import dvaTest.gui.widgets.inputs.CTestInput;
import dvaTest.gui.widgets.inputs.TestInput;
import dvaTest.testCore.TestController;
import dvaTest.testCore.TestPref;
import dvaTest.testCore.TestType;
import dvaTest.testCore.TestTypeException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

public class TestView implements Initializable {

    @FXML
    Pane inputContainer;

    private TestInput testInput;

    private ResourceBundle bundle;
    private TestController testController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.bundle = resourceBundle;
    }

    public void setup(TestPref testPref) {
        testController = new TestController(testPref, this);

        testInput = makeTestInput(testPref, testController);
        inputContainer.getChildren().add(testInput);
    }

    public void start() {
        testController.start();
    }

    @FXML
    void stopTest() {
        testController.stop();
    }

    static TestInput makeTestInput(TestPref testPref) {
        return makeTestInput(testPref, null);
    }

    static TestInput makeTestInput(TestPref testPref, TestController testController) {
        TestInput testInput;
        if (testPref.getTestType() == TestType.C_CHART) {
            testInput = new CTestInput();
        } else {
            throw new TestTypeException("No such test type");
        }
        testInput.setTestController(testController);

        return testInput;
    }
}
