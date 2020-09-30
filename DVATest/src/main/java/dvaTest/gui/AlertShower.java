package dvaTest.gui;

import dvaTest.gui.widgets.CustomAlert;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ResourceBundle;

public class AlertShower {

    public void showError(String content, ResourceBundle bundle, Stage parentStage) {
        try {
            FXMLLoader loader =
                    new FXMLLoader(getClass().getResource("/dvaTest/fxml/widgets/customAlert.fxml"), bundle);
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(parentStage);
            stage.setTitle(bundle.getString("error"));
            stage.setScene(new Scene(root));

            CustomAlert customAlert = loader.getController();
            customAlert.setup(content, stage);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showError(String content, ResourceBundle bundle) {
        showError(content, bundle, null);
    }
}
