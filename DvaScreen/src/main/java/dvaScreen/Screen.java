package dvaScreen;

import dvaScreen.connection.ServerManager;
import dvaScreen.gui.ScreenMainView;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.ResourceBundle;

public class Screen extends Application {
    private static ResourceBundle bundle;

    @Override
    public void start(Stage primaryStage) throws Exception {
        bundle = ResourceBundle.getBundle("dvaScreen.bundles.Languages",
                new Locale("zh", "CN"));

        FXMLLoader loader =
                new FXMLLoader(getClass().getResource("/dvaScreen/fxml/screenMainView.fxml"), bundle);
        Parent root = loader.load();

        ScreenMainView controller = loader.getController();
        controller.setStage(primaryStage);

        primaryStage.setTitle(bundle.getString("appName"));
        primaryStage.setScene(new Scene(root));

        primaryStage.show();

        if (!ServerManager.initialize()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(bundle.getString("error"));
            alert.setContentText(bundle.getString("cannotConnectToNet"));
            return;
        }

        controller.askConnectionIfNone();
    }
}
