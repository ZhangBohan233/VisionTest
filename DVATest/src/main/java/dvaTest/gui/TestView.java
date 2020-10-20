package dvaTest.gui;

import dvaTest.gui.widgets.inputs.CTestInput;
import dvaTest.gui.widgets.inputs.TestInput;
import dvaTest.testCore.*;
import dvaTest.testCore.tests.TestUnit;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class TestView implements Initializable {

    @FXML
    Pane inputContainer;

    @FXML
    Label levelLabel, inputLabel;

    private TestInput testInput;

    private ResourceBundle bundle;
    private Stage thisStage;
    private TestController testController;

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.bundle = resourceBundle;
    }

    public void setup(Stage stage, TestPref testPref) {
        this.thisStage = stage;
        testController = new TestController(testPref, this);

        testInput = makeTestInput(testPref, testController);
        inputContainer.getChildren().add(testInput);
    }

    public void start() {
        testController.start();
    }

    public void updateGui(TestUnit testUnit) {
        Platform.runLater(() -> {
            levelLabel.setText(String.valueOf(testUnit.getVisionLevel()));
        });
    }

    public void updateInput(String input) {
        Platform.runLater(() -> inputLabel.setText(input));
    }

    public void showResult(ResultRecord resultRecord) {
        Platform.runLater(() -> {
            try {
                FXMLLoader loader =
                        new FXMLLoader(getClass().getResource("/dvaTest/fxml/resultView.fxml"),
                                bundle);
                Parent root = loader.load();

                Stage windowStage = new Stage();
                windowStage.setScene(new Scene(root));

                ResultView resultView = loader.getController();
                resultView.setup(windowStage, resultRecord);

                windowStage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void closeWindow() {
        Platform.runLater(() -> thisStage.close());
    }

    @FXML
    void stopTest() {
        testController.stop();
    }
}
