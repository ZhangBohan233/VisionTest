package dvaScreen.gui;

import dvaScreen.connection.ServerManager;
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

    private Stage stage;
    private ResourceBundle bundle;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.bundle = resourceBundle;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void askConnectionIfNone() {
        if (ServerManager.getCurrentServer() == null) {
            showConnectionView();
        }
    }

    void showConnectionView() {
        try {
            FXMLLoader loader =
                    new FXMLLoader(getClass().getResource("/dvaScreen/fxml/screenConnectionView.fxml"), bundle);
            Parent root = loader.load();

            Stage newStage = new Stage();
            newStage.initOwner(stage);
            newStage.initModality(Modality.WINDOW_MODAL);

            newStage.setTitle(bundle.getString("connectComputer"));
            newStage.setScene(new Scene(root));

            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
