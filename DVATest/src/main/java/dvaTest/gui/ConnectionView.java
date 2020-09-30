package dvaTest.gui;

import dvaTest.connection.ClientManager;
import dvaTest.data.CacheSaver;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ConnectionView implements Initializable {

    @FXML
    TextField ipField, portField;

    @FXML
    Label msgLabel;

    private MainView parent;
    private Stage stage;
    private ResourceBundle bundle;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.bundle = resourceBundle;

        portField.setText(CacheSaver.getLastUsedPort());
    }

    @FXML
    void onConnectClicked() {
        int port;
        try {
            port = Integer.parseInt(portField.getText());
        } catch (NumberFormatException e) {
            msgLabel.setTextFill(Paint.valueOf("red"));
            msgLabel.setText(bundle.getString("invalidPortInput"));
            return;
        }
        String ipAddress = ipField.getText();
        try {
            ClientManager.startClient(ipAddress, port);

            Platform.runLater(() -> {
                msgLabel.setTextFill(Paint.valueOf("black"));
                msgLabel.setText(bundle.getString("connectionSuccess"));
            });
            parent.setConnected();
            waitAndExit(0);
        } catch (IOException e) {
            msgLabel.setTextFill(Paint.valueOf("red"));
            msgLabel.setText(bundle.getString("connectionFail"));
        }
    }

    public void setStage(Stage stage, MainView parent) {
        this.stage = stage;
        this.parent = parent;
    }

    private void waitAndExit(long mills) {
        try {
            Thread.sleep(mills);
            stage.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
