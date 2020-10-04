package dvaTest.gui;

import common.EventLogger;
import dvaTest.Main;
import dvaTest.testCore.TestType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainView implements Initializable {

    @FXML
    TitledPane distVisionPane;

    @FXML
    HBox needDisplayDeviceBox;

    @FXML
    Label frameTimeValueLabel;

    @FXML
    Slider frameTimeSlider;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setFrameTimeSliderListener();
        refreshFrameTimeLabel(frameTimeSlider.getValue());
    }

    @FXML
    void onConnectionClick() throws IOException {
        Stage stage = new Stage();

        FXMLLoader loader =
                new FXMLLoader(getClass().getResource("/dvaTest/fxml/connectionView.fxml"),
                        Main.getBundle());
        Parent root = loader.load();

        stage.setTitle(Main.getBundle().getString("connectionTitle"));
        stage.setScene(new Scene(root));

        ConnectionView connectionView = loader.getController();
        connectionView.setStage(stage, this);

        stage.show();
    }

    @FXML
    void snellenChartClicked() {
        showTestView(TestType.SNELLEN_CHART);
    }

    @FXML
    void cChartClicked() {
        showTestView(TestType.C_CHART);
    }

    @FXML
    void eChartClicked() {
        showTestView(TestType.E_CHART);
    }

    void setConnected() {
        needDisplayDeviceBox.setManaged(false);
        needDisplayDeviceBox.setVisible(false);

        distVisionPane.setDisable(false);
        distVisionPane.setExpanded(true);
    }

    private void showTestView(TestType testType) {
        try {
            Stage stage = new Stage();

            FXMLLoader loader =
                    new FXMLLoader(getClass().getResource("/dvaTest/fxml/testView.fxml"),
                            Main.getBundle());
            Parent root = loader.load();

            stage.setTitle(Main.getBundle().getString("connectionTitle"));
            stage.setScene(new Scene(root));

            TestView testView = loader.getController();
            testView.setTestType(testType);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            EventLogger.log(e);
        }
    }

    private void setFrameTimeSliderListener() {
        frameTimeSlider.valueProperty().addListener(((observableValue, number, t1) ->
                refreshFrameTimeLabel(t1.doubleValue())));
    }

    private void refreshFrameTimeLabel(double value) {
        int d = (int) value;
        double frac = value - d;
        double resFrac = Math.round(frac * 10 / 5) * 5;
        String res = String.format("%.1f", resFrac / 10 + d);

        frameTimeValueLabel.setText(res);
    }
}
