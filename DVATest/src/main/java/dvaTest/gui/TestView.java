package dvaTest.gui;

import dvaTest.testCore.TestController;
import dvaTest.testCore.TestPref;
import dvaTest.testCore.TestType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

public class TestView implements Initializable {

    private ResourceBundle bundle;
    private TestController testController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.bundle = resourceBundle;
    }

    public void setup(TestPref testPref) {
        testController = new TestController(testPref, this);
    }

    public void start() {
        testController.start();
    }

    @FXML
    void stopTest() {
        testController.stop();
    }
}
