package dvaTest.gui;

import dvaTest.gui.widgets.inputs.CTestInput;
import dvaTest.gui.widgets.inputs.StdLogTestInput;
import dvaTest.gui.widgets.inputs.TestInput;
import dvaTest.testCore.*;
import dvaTest.testCore.tests.TestUnit;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class TestView implements Initializable {

    @FXML
    Pane rootPane;

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
        // TODO: test type
        if (testPref.getTestType() == TestType.C_CHART) {
            testInput = new CTestInput();
        } else if (testPref.getTestType() == TestType.STD_LOG_CHART) {
            testInput = new StdLogTestInput();
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

        setAllOnKeyPressed(rootPane);
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

    @FXML
    void keyPressedAction(KeyEvent keyEvent) {
//        keyEvent.consume();
//        System.out.println(keyEvent);
        testInput.keyPressed(keyEvent);
    }

    private void setAllOnKeyPressed(Node node) {
        node.setOnKeyPressed(this::keyPressedAction);
        if (node instanceof Pane) {
            for (Node child : ((Pane) node).getChildren()) {
                setAllOnKeyPressed(child);
            }
        }
    }
}
