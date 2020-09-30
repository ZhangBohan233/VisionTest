package dvaTest.gui.widgets;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class CustomAlert implements Initializable {

    @FXML
    Label contentLabel;

    private Stage stage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    void onOkAction() {
        stage.close();
    }

    public void setup(String text, Stage thisStage) {
        contentLabel.setText(text);
        this.stage = thisStage;
    }
}
