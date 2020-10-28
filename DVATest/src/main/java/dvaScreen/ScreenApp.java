package dvaScreen;

import common.EventLogger;
import common.data.AutoSavers;
import dvaScreen.connection.ServerManager;
import dvaScreen.gui.ScreenMainView;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class ScreenApp extends Application {
    private static ResourceBundle bundle;

    public static void run(String[] args) {
        try {
            launch(args);
        } catch (Exception e) {
            e.printStackTrace();
            EventLogger.log(e);
        }
    }

    public static ResourceBundle getBundle() {
        return bundle;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        AutoSavers.startScreenSavers();

        bundle = ResourceBundle.getBundle("common.bundles.Languages",
                new Locale("zh", "CN"));

        FXMLLoader loader =
                new FXMLLoader(getClass().getResource("/dvaScreen/fxml/screenMainView.fxml"), bundle);
        Parent root = loader.load();

        ScreenMainView controller = loader.getController();
        controller.setup(primaryStage);

        primaryStage.setTitle(bundle.getString("appNameScreen"));
        primaryStage.setScene(new Scene(root));

        if (!ServerManager.startServer(controller)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(bundle.getString("error"));
            alert.setContentText(bundle.getString("cannotConnectToNet"));
            return;
        }

        primaryStage.setOnHidden(e -> {
            try {
                AutoSavers.stopAllSavers();
                ServerManager.stopServer();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        primaryStage.show();
        controller.askConnectionIfNone();
    }
}
