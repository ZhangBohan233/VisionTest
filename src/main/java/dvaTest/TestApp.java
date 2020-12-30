package dvaTest;

import common.EventLogger;
import common.data.AutoSavers;
import dvaTest.connection.ClientManager;
import dvaTest.gui.MainView;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * 输入端应用
 */
public class TestApp extends Application {

    public static final String VERSION = "V1.0";
    public static final String AUTHOR_ZH = "张博涵";
    public static final String AUTHOR_EN = "Bohan Zhang";

    private static Image icon;
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
        primaryStage.getIcons().add(getIcon());
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

    public static Image getIcon() throws IOException {
        if (icon == null) {
            InputStream iconInputStream = TestApp.class.getResourceAsStream("/common/images/icon.jpg");
            icon = new Image(iconInputStream);
            iconInputStream.close();
        }
        return icon;
    }
}
