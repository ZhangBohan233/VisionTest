package dvaTest.gui.widgets;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class CustomAlert implements Initializable {

    @FXML
    Label contentLabel;

    @FXML
    Button cancelButton;

    private Stage stage;

    private ButtonType buttonType = ButtonType.NONE;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    void onOkAction() {
        buttonType = ButtonType.OK;
        stage.close();
    }

    @FXML
    void onCancelAction() {
        buttonType = ButtonType.CANCEL;
        stage.close();
    }

    public void setup(String text, Stage thisStage, boolean showCancel) {
        contentLabel.setText(text);
        this.stage = thisStage;
        if (showCancel) {
            cancelButton.setManaged(true);
        }
    }

    public boolean isOkClicked() {
        return buttonType == ButtonType.OK;
    }

    enum ButtonType {
        OK,
        CANCEL,
        NONE
    }
}
