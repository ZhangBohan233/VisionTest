package dvaTest.gui;

import dvaTest.TestApp;
import dvaTest.connection.ClientManager;
import dvaTest.gui.widgets.inputs.*;
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
    Label levelLabel, inputLabel, eyeLabel;

    private TestInput testInput;

    private ResourceBundle bundle;
    private Stage thisStage;
    private Scene thisScene;
    private TestPrepView testPrepView;
    private TestController testController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.bundle = resourceBundle;
    }

    public void setup(Stage stage, TestPrepView testPrepView, TestController testController, TestPref testPref) {
        this.thisStage = stage;
        this.testPrepView = testPrepView;
        this.thisScene = stage.getScene();
        this.testController = testController;

        ClientManager.getCurrentClient().setTestController(testController);

        testInput = testPref.getTestType().generateTestInput(testController);
        inputContainer.getChildren().add(testInput);

        setAllOnKeyPressed(rootPane);

        thisStage.sizeToScene();

//        thisStage.setOnCloseRequest(e -> {
//            testController.stopByUser();
//        });
    }

    public Scene getScene() {
        return thisScene;
    }

    public void start() {
        testController.startPartialTest();
    }

    public void updateGui(TestUnit testUnit) {
        Platform.runLater(() -> {
            levelLabel.setText(testUnit.getVisionLevel());
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
                windowStage.getIcons().add(TestApp.getIcon());
                windowStage.setScene(new Scene(root));

                ResultView resultView = loader.getController();
                resultView.setup(windowStage, resultRecord);

                windowStage.sizeToScene();
                windowStage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void nextSideTest(EyeSide eyeSide) {
        testPrepView.backToPrepView(eyeSide);
    }

    public void setEyeLabel(EyeSide eyeSide) {
        eyeLabel.setText(eyeSide.toString());
    }

    public void closeWindow() {
        Platform.runLater(() -> thisStage.close());
    }

    @FXML
    void stopTest() {
        testController.stopByUser();
        thisStage.close();
    }

    @FXML
    void keyPressedAction(KeyEvent keyEvent) {
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
