package dvaTest.gui;

import dvaTest.TestApp;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class AboutView implements Initializable {
    @FXML
    Label versionLabel, authorLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        versionLabel.setText(TestApp.VERSION);
        setAuthor(resources);
    }

    private void setAuthor(ResourceBundle bundle) {
        if (bundle.getLocale().getLanguage().equals("zh")) {
            authorLabel.setText(TestApp.AUTHOR_ZH);
        } else {
            authorLabel.setText(TestApp.AUTHOR_EN);
        }
    }
}
