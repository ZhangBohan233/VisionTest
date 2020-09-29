package dvaScreen.gui;

import dvaScreen.connection.ServerManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class ScreenConnectionView implements Initializable {

    @FXML
    Label thisIpLabel, portLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        thisIpLabel.setText(ServerManager.getThisAddress().getHostAddress());
        portLabel.setText(String.valueOf(ServerManager.getPort()));
    }
}
