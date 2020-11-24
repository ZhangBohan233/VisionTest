package dvaScreen.gui;

import common.Utility;
import common.data.AutoSavers;
import common.data.CacheSaver;
import dvaScreen.connection.ServerManager;
import dvaScreen.gui.items.ResolutionItem;
import dvaScreen.gui.items.WindowsScaleItem;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ScreenMainView implements Initializable {

    @FXML
    ImageView logoView;

    @FXML
    Label thisIpLabel, thisNameLabel, portLabel, screenPpiLabel;

    @FXML
    GridPane notConnectedPane;

    @FXML
    HBox connectedPane;

    @FXML
    ComboBox<ResolutionItem> resolutionBox;

    @FXML
    ComboBox<WindowsScaleItem> systemZoomBox;

    @FXML
    TextField fracField;

    @FXML
    Spinner<Integer> intSpinner;

    SpinnerValueFactory<Integer> intFactory;

    private Stage stage;
    private ResourceBundle bundle;

    private static double calculatePpiRounded(int width, int height, double screenSize) {
        return Utility.round(Math.sqrt(height * height + width * width) / screenSize, 1);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.bundle = resourceBundle;

        setSpinners();
        addPpiGroupListeners();
        fillBoxes();

        restoreFromCache();
        setAutoDetected();
    }

    public void setup(Stage stage) {
        this.stage = stage;
    }

    public Stage getStage() {
        return stage;
    }

    public void showCannotConnect(String header, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);

            alert.setTitle(bundle.getString("error"));
            alert.setHeaderText(header);
            alert.setContentText(message);
            alert.initOwner(stage);
            alert.initModality(Modality.WINDOW_MODAL);
            alert.show();

            alert.setOnCloseRequest(e -> {
                stage.close();
            });
        });
    }

    public void askConnectionIfNone() {
        thisNameLabel.setText(ServerManager.getThisAddress().getHostName());
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
        return systemZoomBox.getValue().getScale();
    }

    private void fillBoxes() {
        resolutionBox.getItems().addAll(ResolutionItem.RESOLUTION_ITEMS);
        resolutionBox.getSelectionModel().select(0);

        systemZoomBox.getItems().addAll(WindowsScaleItem.SCALE_ITEMS);
        systemZoomBox.getSelectionModel().select(0);
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
        intFactory.setValue(24);

        intSpinner.valueProperty().addListener(((observable, oldValue, newValue) -> {
            AutoSavers.getCacheSaver().putCache(CacheSaver.SCREEN_SIZE, getScreenSize());
        }));
    }

    private void setAutoDetected() {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
//        double windowsWidth = screen.getWidth();
        double windowsHeight = screen.getHeight();
        DisplayMode mode = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode();
        int hardwareWidth = mode.getWidth();
        int hardwareHeight = mode.getHeight();

        double zoomFactor = hardwareHeight / windowsHeight;

        setAutoDetectedResolution(hardwareWidth, hardwareHeight);
        setAutoDetectedScale(zoomFactor);
    }

    private void setAutoDetectedResolution(int width, int height) {
        int index = 0;
        for (ResolutionItem ri : resolutionBox.getItems()) {
            if (ri.getWidth() == width && ri.getHeight() == height) {
                ri.setAutoDetected(true);
                resolutionBox.getSelectionModel().select(index);
                return;
            }
            index++;
        }
        ResolutionItem ri = new ResolutionItem(width, height);
        ri.setAutoDetected(true);
        resolutionBox.getItems().add(0, ri);
        resolutionBox.getSelectionModel().select(0);
    }

    private void setAutoDetectedScale(double scale) {
        int index = 0;
        for (WindowsScaleItem wsi : systemZoomBox.getItems()) {
            if (wsi.getScale() == scale) {
                wsi.setAutoDetected(true);
                systemZoomBox.getSelectionModel().select(index);
                return;
            }
            index++;
        }
        WindowsScaleItem wsi = new WindowsScaleItem(scale);
        wsi.setAutoDetected(true);
        systemZoomBox.getItems().add(0, wsi);
        systemZoomBox.getSelectionModel().select(0);
    }

    private void addPpiGroupListeners() {
        fracField.textProperty().addListener(((observableValue, s, t1) -> {
            if (t1.length() > 0) {
                try {
                    Integer.parseInt(t1);
                    fracField.setText(t1);
                    AutoSavers.getCacheSaver().putCache(CacheSaver.SCREEN_SIZE, getScreenSize());
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
        String fracWithDot = String.valueOf(Utility.round(value - intPart, 2));
        String frac = fracWithDot.substring(fracWithDot.indexOf('.') + 1);
        fracField.setText(frac);
    }

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

    private void restoreFromCache() {
        double screenSize = AutoSavers.getCacheSaver().getDouble(CacheSaver.SCREEN_SIZE);
        setScreenSize(Double.isNaN(screenSize) ? 15.6 : screenSize);
    }

//    public void storeToCache() {
////        CacheSaver.ScreenCache.writeScreenSize(getScreenSize());
//        AutoSavers.getCacheSaver().putCache(CacheSaver.SCREEN_SIZE, getScreenSize());
//    }
}
