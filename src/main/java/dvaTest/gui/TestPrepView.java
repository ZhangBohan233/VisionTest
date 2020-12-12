package dvaTest.gui;

import common.EventLogger;
import dvaTest.connection.ClientManager;
import dvaTest.testCore.EyeSide;
import dvaTest.testCore.IdleTestController;
import dvaTest.testCore.TestController;
import dvaTest.testCore.TestPref;
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
import java.util.ResourceBundle;

public class TestPrepView implements Initializable {

    @FXML
    Pane inputContainer;

    @FXML
    Label eyeLabel;

    private TestPref testPref;
    private ResourceBundle bundle;
    private Stage stage;
    private Scene scene;
    private TestView testView;

    private TestController realController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.bundle = resourceBundle;
    }

    public void setup(TestPref testPref, Stage stage) throws IOException {
        this.testPref = testPref;
        this.stage = stage;
        this.scene = stage.getScene();

        this.realController = new TestController(testPref);

        // 使client可以关闭这个窗口
        ClientManager.getCurrentClient().setTestController(new IdleTestController(this));

        inputContainer.getChildren().add(testPref.getTestType().generateTestInput(null));
        setEyeLabel(realController.getNextSide());

        ClientManager.getCurrentClient().sendMessage(testPref.getTestType().getSignal());
        try {
            Thread.sleep(100);
            ClientManager.getCurrentClient().sendMessage(realController.getNextSide().toBytes());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        stage.setOnCloseRequest(e -> realController.stopByUser());
    }

    @FXML
    void startTestClicked() {
        if (testView == null) {
            try {
                FXMLLoader loader =
                        new FXMLLoader(getClass().getResource("/dvaTest/fxml/testView.fxml"),
                                bundle);
                Parent root = loader.load();

                Scene scene = new Scene(root);

                stage.setScene(scene);

                testView = loader.getController();
                realController.setTestView(testView);
                testView.setup(stage, this, realController, testPref);

                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                EventLogger.log(e);
            }
        } else {
            Platform.runLater(() -> {
                stage.setScene(testView.getScene());
            });
        }
        testView.start();
    }

    public void backToPrepView(EyeSide eyeSide) {
        Platform.runLater(() -> {
            stage.setScene(scene);
            setEyeLabel(eyeSide);
        });
    }

    private void setEyeLabel(EyeSide eyeSide) {
        eyeLabel.setText(eyeSide.toString());
    }

    public void closeWindow() {
        Platform.runLater(() -> stage.close());
    }
}
