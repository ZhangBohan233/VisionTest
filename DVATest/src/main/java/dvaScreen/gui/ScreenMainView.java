package dvaScreen.gui;

import common.Utility;
import dvaScreen.connection.ServerManager;
import dvaScreen.gui.items.ResolutionItem;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ScreenMainView implements Initializable {

    @FXML
    ImageView logoView;

    @FXML
    Label thisIpLabel, portLabel, screenPpiLabel;

    @FXML
    GridPane notConnectedPane;

    @FXML
    HBox connectedPane;

    @FXML
    ComboBox<ResolutionItem> resolutionBox;

    @FXML
    ComboBox<String> systemZoomBox;

    @FXML
    TextField fracField;

    @FXML
    Spinner<Integer> intSpinner;

    SpinnerValueFactory<Integer> intFactory;

    private Stage stage, connectionStage;
    private ResourceBundle bundle;

    private static double calculatePpiRounded(int width, int height, double screenSize) {
        return Utility.round(Math.sqrt(height * height + width * width) / screenSize, 1);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.bundle = resourceBundle;

        setSpinners();
        addPpiGroupListeners();
        fillResolutionBox();
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

    public double getPpi() {
        return Double.parseDouble(screenPpiLabel.getText());
    }

    public double getPixelsPerMm() {
        double screenSize = getScreenSize();
        ResolutionItem ri = resolutionBox.getValue();
        double ratio = (double) ri.getHeight() / ri.getWidth();
        double heightInches = Math.sqrt(screenSize * screenSize / (1 + 1 / (ratio * ratio)));
        double res = ri.getHeight() / (heightInches * 25.4);
        System.out.println(heightInches + " " + res);
        return res;
    }

    public double getSystemZoom() {
        String zoomStringPer = systemZoomBox.getValue();
        String zoomString = zoomStringPer.substring(0, zoomStringPer.length() - 1);
        return Double.parseDouble(zoomString) / 100;
    }

    private void fillResolutionBox() {
        resolutionBox.getItems().addAll(ResolutionItem.resolutionItems);
        resolutionBox.getSelectionModel().select(ResolutionItem.DEFAULT_INDEX);
    }

    private void setSpinners() {
        intFactory = new SpinnerValueFactory<>() {
            @Override
            public void decrement(int steps) {
                setValue(getValue() - steps);
                updatePpi();
            }

            @Override
            public void increment(int steps) {
                setValue(getValue() + steps);
                updatePpi();
            }
        };

        intSpinner.setValueFactory(intFactory);
        intFactory.setValue(15);
    }

    private void addPpiGroupListeners() {
        fracField.textProperty().addListener(((observableValue, s, t1) -> {
            if (t1.length() > 0) {
                try {
                    Integer.parseInt(t1);
                    fracField.setText(t1);
                } catch (NumberFormatException nfe) {
                    fracField.setText(s);
                }
            }
            updatePpi();
        }));

        resolutionBox.getSelectionModel().selectedItemProperty()
                .addListener(((observableValue, resolutionItem, t1) -> updatePpi()));
    }

    private double getScreenSize() {
        int inches = intSpinner.getValue();
        String frac = "0." + fracField.getText();
        return inches + Double.parseDouble(frac);
    }

    private void updatePpi() {
        ResolutionItem ri = resolutionBox.getValue();
        screenPpiLabel.setText(String.valueOf(calculatePpiRounded(ri.getWidth(), ri.getHeight(),
                getScreenSize())));
    }

    private void setScreenSize(double value) {
        int intPart = (int) value;
        intFactory.setValue(intPart);
        fracField.setText(String.valueOf(value - intPart).substring(2));
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
