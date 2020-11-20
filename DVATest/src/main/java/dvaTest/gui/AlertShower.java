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

    public static void showError(String content, ResourceBundle bundle, Stage parentStage) {
        show(content, bundle.getString("error"), bundle, parentStage);
    }

    public static void showSuccess(String content, ResourceBundle bundle, Stage parentStage) {
        show(content, bundle.getString("success"), bundle, parentStage);
    }

    private static void show(String content, String title, ResourceBundle bundle, Stage parentStage) {
        try {
            FXMLLoader loader =
                    new FXMLLoader(AlertShower.class.getResource("/dvaTest/fxml/widgets/customAlert.fxml"), bundle);
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(parentStage);
            stage.setTitle(title);
            stage.setScene(new Scene(root));

            CustomAlert customAlert = loader.getController();
            customAlert.setup(content, stage);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
