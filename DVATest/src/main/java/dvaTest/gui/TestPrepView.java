package dvaTest.gui;

import common.EventLogger;
import dvaTest.gui.widgets.inputs.CTestInput;
import dvaTest.gui.widgets.inputs.TestInput;
import dvaTest.testCore.TestPref;
import dvaTest.testCore.TestType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class TestPrepView implements Initializable {

    @FXML
    Pane inputContainer;

    private TestPref testPref;
    private ResourceBundle bundle;
    private Stage stage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.bundle = resourceBundle;
    }

    public void setTestPref(TestPref testPref, Stage stage) {
        this.testPref = testPref;
        this.stage = stage;

        inputContainer.getChildren().add(TestView.makeTestInput(testPref));
    }

    @FXML
    void startTestClicked() {
        try {
            FXMLLoader loader =
                    new FXMLLoader(getClass().getResource("/dvaTest/fxml/testView.fxml"),
                            bundle);
            Parent root = loader.load();

            stage.setTitle(testPref.getTestType().show(bundle));
            stage.setScene(new Scene(root));

            TestView testView = loader.getController();
            testView.setup(stage, testPref);

            stage.show();

            testView.start();
        } catch (IOException e) {
            e.printStackTrace();
            EventLogger.log(e);
        }
    }
}
