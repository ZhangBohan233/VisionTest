package dvaScreen.gui;

import dvaScreen.connection.ServerManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class ScreenMainView implements Initializable {

    @FXML
    ImageView logoView;

    @FXML
    Label thisIpLabel, portLabel;

    @FXML
    GridPane notConnectedPane;

    @FXML
    HBox connectedPane;

    private Stage stage, connectionStage;
    private ResourceBundle bundle;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.bundle = resourceBundle;
//        InputStream inputStream = getClass().getResourceAsStream("/common/images/c/C1.png");
//        Image image = new Image(inputStream);
//
//        logoView.setPreserveRatio(true);
//        logoView.setFitWidth(800.0);
//        logoView.setImage(image);
    }

    public void setup(Stage stage) {
        this.stage = stage;
    }

    public void askConnectionIfNone() {
        thisIpLabel.setText(ServerManager.getThisAddress().getHostAddress());
        portLabel.setText(String.valueOf(ServerManager.getPort()));
        if (!ServerManager.hasConnection()) {
            setDisconnectedUi();
        }
    }

//    void showConnectionView() {
//        try {
//            FXMLLoader loader =
//                    new FXMLLoader(getClass().getResource("/dvaScreen/fxml/screenConnectionView.fxml"), bundle);
//            Parent root = loader.load();
//
//            connectionStage = new Stage();
//            connectionStage.initOwner(stage);
//            connectionStage.initModality(Modality.WINDOW_MODAL);
//
//            connectionStage.setTitle(bundle.getString("connectComputer"));
//            connectionStage.setScene(new Scene(root));
//
//            connectionStage.show();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    public void setConnectedUi() {
        connectedPane.setVisible(true);
        connectedPane.setManaged(true);
        notConnectedPane.setVisible(false);
        notConnectedPane.setManaged(false);
    }

    public void setDisconnectedUi() {
        connectedPane.setVisible(false);
        connectedPane.setManaged(false);
        notConnectedPane.setVisible(true);
        notConnectedPane.setManaged(true);
    }

    public ResourceBundle getBundle() {
        return bundle;
    }
}
