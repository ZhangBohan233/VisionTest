package dvaTest.gui;

import common.data.AutoSavers;
import dvaScreen.connection.ServerManager;
import dvaTest.connection.ClientManager;
import common.data.CacheSaver;
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

        addIpFieldListener();
        addPortFieldListener();

        restoreFromCache();
    }

    @FXML
    void onConnectClicked() {
        String portText;
        int port;
        try {
            portText = portField.getText();
            port = Integer.parseInt(portText);
        } catch (NumberFormatException e) {
            msgLabel.setTextFill(Paint.valueOf("red"));
            msgLabel.setText(bundle.getString("invalidPortInput"));
            return;
        }
        String ipAddress = ipField.getText();
//        CacheSaver.TestCache.writePortAndIp(portText, ipAddress);
        try {
            ClientManager.startClient(ipAddress, port, parent);

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

    private void addIpFieldListener() {
        ipField.textProperty().addListener(((observableValue, s, t1) -> {
            for (int i = 0; i < t1.length(); i++) {
                char newChar = t1.charAt(i);
                if ((newChar < '0' || newChar > '9') && newChar != '.') {
                    ipField.setText(s);
                    // 这里不用保存至cache
                    return;
                }
            }
            ipField.setText(t1);
            AutoSavers.getCacheSaver().putCache(CacheSaver.TEST_IP, t1);
        }));
    }

    private void addPortFieldListener() {
        portField.textProperty().addListener(((observableValue, s, t1) -> {
            for (int i = 0; i < t1.length(); i++) {
                char newChar = t1.charAt(i);
                if (newChar < '0' || newChar > '9') {
                    portField.setText(s);
                    // 这里不用保存至cache
                    return;
                }
            }
            portField.setText(t1);
            AutoSavers.getCacheSaver().putCache(CacheSaver.TEST_PORT, t1);
        }));
    }

    private void restoreFromCache() {
        String port = AutoSavers.getCacheSaver().getCache(CacheSaver.TEST_PORT);
        String ip = AutoSavers.getCacheSaver().getCache(CacheSaver.TEST_IP);
        portField.setText(port == null ? String.valueOf(ServerManager.DEFAULT_PORT) : port);
        ipField.setText(ip == null ? ClientManager.DEFAULT_IP : ip);
//        String[] portIp = CacheSaver.TestCache.getLastUsedPortAndIp();
//        portField.setText(portIp[0]);
//        ipField.setText(portIp[1]);
    }
}
