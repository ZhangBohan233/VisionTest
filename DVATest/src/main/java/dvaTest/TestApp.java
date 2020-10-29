package dvaTest;

import common.EventLogger;
import common.data.AutoSavers;
import dvaTest.connection.ClientManager;
import dvaTest.gui.MainView;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public class TestApp extends Application {

    private static ResourceBundle bundle;
    private static SimpleDateFormat dateFormat;

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
        AutoSavers.startTestSavers();

        bundle = ResourceBundle.getBundle("common.bundles.Languages",
                new Locale("zh", "CN"));
        dateFormat = new SimpleDateFormat(bundle.getString("dateFormat"));

        FXMLLoader loader =
                new FXMLLoader(getClass().getResource("/dvaTest/fxml/mainView.fxml"), bundle);
        Parent root = loader.load();

        primaryStage.setTitle(bundle.getString("appName"));
        primaryStage.setScene(new Scene(root));

        MainView mainView = loader.getController();
        mainView.setStage(primaryStage);

        primaryStage.setOnHidden(e -> {
            try {
                AutoSavers.stopAllSavers();
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

    public static SimpleDateFormat getDateFormat() {
        return dateFormat;
    }
}
