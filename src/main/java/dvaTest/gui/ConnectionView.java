package dvaTest.gui;

import common.data.AutoSavers;
import common.data.CacheSaver;
import dvaScreen.connection.ServerManager;
import dvaTest.connection.ClientManager;
import dvaTest.connection.DeviceItem;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.util.ResourceBundle;

public class ConnectionView implements Initializable {

    @FXML
    TextField ipField, portField;

    @FXML
    Label msgLabel, deviceListPlaceHolder;

    @FXML
    TableView<DeviceItem> devicesTable;

    ScanDevicesService scanService;

    private MainView parent;
    private Stage stage;
    private ResourceBundle bundle;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.bundle = resourceBundle;

        addIpFieldListener();
        addPortFieldListener();
        setDevicesTableFactory();

        restoreFromCache();
    }

    @FXML
    void onConnectClicked() {
        int port = getPort();
        if (port == -1) return;
        String ipAddress = ipField.getText();
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

    @FXML
    void onRefreshClicked() {
        devicesTable.getItems().clear();
        deviceListPlaceHolder.setText(bundle.getString("searchingDevices"));
        startFindingDevices();
    }

    public void setup(Stage stage, MainView parent) {
        this.stage = stage;
        this.parent = parent;

        stage.setOnHidden(e -> {
            if (scanService != null) {
                scanService.cancel();
            }
        });

        onRefreshClicked();
    }

    private int getPort() {
        try {
            String portText = portField.getText();
            return Integer.parseInt(portText);
        } catch (NumberFormatException e) {
            msgLabel.setTextFill(Paint.valueOf("red"));
            msgLabel.setText(bundle.getString("invalidPortInput"));
            return -1;
        }
    }

    private void waitAndExit(long mills) {
        try {
            Thread.sleep(mills);
            stage.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void startFindingDevices() {
        if (scanService != null && scanService.isRunning()) {
            scanService.cancel();  // 取消当前服务
        }
        scanService = new ScanDevicesService();
        scanService.setOnCancelled(e -> {
            scanService.runningProperty.setValue(false);
            deviceListPlaceHolder.setText(bundle.getString("deviceListPlaceHolder"));
        });
        scanService.setOnFailed(e -> deviceListPlaceHolder.setText(bundle.getString("searchDevicesFailed")));
        scanService.setOnSucceeded(e -> deviceListPlaceHolder.setText(bundle.getString("deviceListPlaceHolder")));
        scanService.start();
    }

    private void setDevicesTableFactory() {
        devicesTable.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("name"));
        devicesTable.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("hostAddress"));

        devicesTable.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue != null) {
                ipField.setText(newValue.getAddress().getHostAddress());
            }
        }));

        devicesTable.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                if (devicesTable.getSelectionModel().getSelectedIndex() != -1)
                    onConnectClicked();
            }
        });
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
    }

    private class ScanDevicesService extends Service<Void> {
        private final BooleanProperty runningProperty = new ReadOnlyBooleanWrapper(true);

        @Override
        protected Task<Void> createTask() {
            return new Task<>() {
                @Override
                protected Void call() {
                    int port = getPort();
                    if (port == -1) {
                        cancel();
                        return null;
                    }
                    ClientManager.listLanDevices(devicesTable.getItems(), port, runningProperty);
                    return null;
                }
            };
        }
    }
}
