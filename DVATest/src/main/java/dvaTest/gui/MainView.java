package dvaTest.gui;

import common.EventLogger;
import common.data.CacheSaver;
import dvaTest.TestApp;
import dvaTest.connection.ClientManager;
import dvaTest.gui.items.ScoreCounting;
import dvaTest.testCore.TestPref;
import dvaTest.testCore.TestType;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
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
    HBox needDisplayDeviceBox, connectionBox;

    @FXML
    Label timeIntervalLabel;

    @FXML
    Slider timeIntervalSlider;

    @FXML
    ComboBox<ScoreCounting> scoreCountingBox;

    private ResourceBundle bundle;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.bundle = resourceBundle;

        setFrameTimeSliderListener();
        refreshFrameTimeLabel(timeIntervalSlider.getValue());

        scoreCountingBox.getItems().addAll(ScoreCounting.FIVE, ScoreCounting.FRAC, ScoreCounting.LOG_MAR);

        restoreFromCache();
    }

    @FXML
    void onConnectionClick() throws IOException {
        Stage stage = new Stage();

        FXMLLoader loader =
                new FXMLLoader(getClass().getResource("/dvaTest/fxml/connectionView.fxml"),
                        TestApp.getBundle());
        Parent root = loader.load();

        stage.setTitle(bundle.getString("connectionTitle"));
        stage.setScene(new Scene(root));

        ConnectionView connectionView = loader.getController();
        connectionView.setStage(stage, this);

        stage.show();
    }

    @FXML
    void onDisconnectClicked() throws IOException {
        ClientManager.closeCurrentClient();
        setDisconnected();
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

    @FXML
    void stdLogChartClicked() {
        showTestView(TestType.STD_LOG_CHART);
    }

    @FXML
    void etdrsCharClicked() {

    }

    void setConnected() {
        needDisplayDeviceBox.setManaged(false);
        needDisplayDeviceBox.setVisible(false);

        connectionBox.setManaged(true);
        connectionBox.setVisible(true);

        distVisionPane.setDisable(false);
        distVisionPane.setExpanded(true);
    }

    public void setDisconnected() {
        Platform.runLater(() -> {
            needDisplayDeviceBox.setManaged(true);
            needDisplayDeviceBox.setVisible(true);

            connectionBox.setManaged(false);
            connectionBox.setVisible(false);

            distVisionPane.setDisable(true);
            distVisionPane.setExpanded(false);
        });
    }

    private long getTimeInterval() {
        return (long) (timeIntervalSlider.getValue() * 1000);
    }

    private void showTestView(TestType testType) {
        TestPref testPref = new TestPref.TestPrefBuilder()
                .testType(testType)
                .scoreCounting(scoreCountingBox.getValue())
                .frameTimeMills(getTimeInterval())
                .build();
        storeCache();
        try {
            Stage stage = new Stage();

            FXMLLoader loader =
                    new FXMLLoader(getClass().getResource("/dvaTest/fxml/testPrepView.fxml"),
                            bundle);
            Parent root = loader.load();

            stage.setTitle(testType.show(bundle));
            stage.setScene(new Scene(root));

            TestPrepView testPrepView = loader.getController();
            testPrepView.setTestPref(testPref, stage);

            stage.show();

            ClientManager.getCurrentClient().sendMessage(testType.getSignal());
        } catch (IOException e) {
            e.printStackTrace();
            EventLogger.log(e);
        }
    }

    private void setFrameTimeSliderListener() {
        timeIntervalSlider.valueProperty().addListener(((observableValue, number, t1) ->
                refreshFrameTimeLabel(t1.doubleValue())));
    }

    private void refreshFrameTimeLabel(double value) {
        int d = (int) value;
        double frac = value - d;
        double resFrac = Math.round(frac * 10 / 5) * 5;
        String res = String.format("%.1f", resFrac / 10 + d);

        timeIntervalLabel.setText(res);
    }

    private void restoreFromCache() {
        scoreCountingBox.getSelectionModel().select(CacheSaver.TestCache.getLastUsedScoreCounting());
        timeIntervalSlider.setValue((double) CacheSaver.TestCache.getLastUsedTimeInterval() / 1000);
    }

    private void storeCache() {
        CacheSaver.TestCache.writeScoreCounting(scoreCountingBox.getValue());
        CacheSaver.TestCache.writeTimeInterval(getTimeInterval());
    }
}
