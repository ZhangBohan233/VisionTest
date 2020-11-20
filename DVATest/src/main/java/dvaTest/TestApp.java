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
    private static SimpleDateFormat fullDateFormat;
    private static SimpleDateFormat dateFormat;
    private static SimpleDateFormat timeFormat;

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
        fullDateFormat = new SimpleDateFormat(bundle.getString("fullDateFormat"));
        dateFormat = new SimpleDateFormat(bundle.getString("dateFormat"));
        timeFormat = new SimpleDateFormat(bundle.getString("timeFormat"));

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
                ClientManager.closeAndDiscardCurrentClient();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        primaryStage.show();
    }

    public static ResourceBundle getBundle() {
        return bundle;
    }

    public static SimpleDateFormat getFullDateFormat() {
        return fullDateFormat;
    }

    public static SimpleDateFormat getDateFormat() {
        return dateFormat;
    }

    public static SimpleDateFormat getTimeFormat() {
        return timeFormat;
    }
}
