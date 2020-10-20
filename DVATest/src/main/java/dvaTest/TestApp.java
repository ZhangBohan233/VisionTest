package dvaTest;

import common.EventLogger;
import dvaTest.connection.ClientManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class TestApp extends Application {

    private static ResourceBundle bundle;

    public static void run(String[] args) {
        try {
            launch(args);
        } catch (Exception e) {
            e.printStackTrace();
            EventLogger.log(e);
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        bundle = ResourceBundle.getBundle("common.bundles.Languages",
                new Locale("zh", "CN"));

        FXMLLoader loader =
                new FXMLLoader(getClass().getResource("/dvaTest/fxml/mainView.fxml"), bundle);
        Parent root = loader.load();

        primaryStage.setTitle(bundle.getString("appName"));
        primaryStage.setScene(new Scene(root));

        primaryStage.setOnHidden(e -> {
            try {
                ClientManager.closeCurrentClient();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        primaryStage.show();
    }

    public static ResourceBundle getBundle() {
        return bundle;
    }
}
