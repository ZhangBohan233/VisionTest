package dvaTest.gui;

import dvaTest.testCore.TestType;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class TestView implements Initializable {

    private TestType testType;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void setTestType(TestType testType) {
        this.testType = testType;
    }
}
