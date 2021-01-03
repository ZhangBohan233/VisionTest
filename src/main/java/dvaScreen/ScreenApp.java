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
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * 屏幕端应用
 */
public class ScreenApp extends Application {
    private static ResourceBundle bundle;
    private static Image icon;
    private static boolean local = false;

    public static void run(String[] args) {
        try {
            launch(args);
        } catch (Exception e) {
            e.printStackTrace();
            EventLogger.log(e);
        }
    }

    public static void runLocal(String[] args) {
        local = true;
        run(args);
    }

    public static ResourceBundle getBundle() {
        return bundle;
    }

    public static Image getIcon() throws IOException {
        if (icon == null) {
            InputStream iconInputStream = ScreenApp.class.getResourceAsStream("/common/images/icon.jpg");
            icon = new Image(iconInputStream);
            iconInputStream.close();
        }
        return icon;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        AutoSavers.startScreenSavers();

        bundle = ResourceBundle.getBundle("common.bundles.Languages",
                new Locale("zh", "CN"));

        InputStream iconInputStream = getClass().getResourceAsStream("/common/images/icon.jpg");
        icon = new Image(iconInputStream);
        iconInputStream.close();

        FXMLLoader loader =
                new FXMLLoader(getClass().getResource("/dvaScreen/fxml/screenMainView.fxml"), bundle);
        Parent root = loader.load();

        ScreenMainView controller = loader.getController();
        controller.setup(primaryStage);

        primaryStage.setTitle(bundle.getString("appNameScreen"));
        primaryStage.getIcons().add(getIcon());
        primaryStage.setScene(new Scene(root));

        if (!ServerManager.startServer(controller, local)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(bundle.getString("error"));
            alert.setContentText(bundle.getString("cannotConnectToNet"));
            return;
        }

        primaryStage.setOnHidden(e -> {
            AutoSavers.stopAllSavers();
            ServerManager.stopServer();
        });

        primaryStage.show();

        if (!local)
            controller.askConnectionIfNone();
    }
}
