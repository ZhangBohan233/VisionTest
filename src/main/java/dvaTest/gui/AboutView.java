package dvaTest.gui;

import dvaTest.TestApp;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class AboutView implements Initializable {
    @FXML
    Label versionLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        versionLabel.setText(TestApp.VERSION);
    }
}
