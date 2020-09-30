package dvaScreen.gui;

import dvaScreen.connection.ServerManager;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ScreenMainView implements Initializable {

    private Stage stage, connectionStage;
    private ResourceBundle bundle;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.bundle = resourceBundle;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void askConnectionIfNone() {
        if (!ServerManager.hasConnection()) {
            showConnectionView();
        }
    }

    void showConnectionView() {
        try {
            FXMLLoader loader =
                    new FXMLLoader(getClass().getResource("/dvaScreen/fxml/screenConnectionView.fxml"), bundle);
            Parent root = loader.load();

            connectionStage = new Stage();
            connectionStage.initOwner(stage);
            connectionStage.initModality(Modality.WINDOW_MODAL);

            connectionStage.setTitle(bundle.getString("connectComputer"));
            connectionStage.setScene(new Scene(root));

            connectionStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeConnectionWindow() {
        Platform.runLater(() -> connectionStage.close());
    }
}
