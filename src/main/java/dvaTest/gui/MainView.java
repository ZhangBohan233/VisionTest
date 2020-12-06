package dvaTest.gui;

import common.EventLogger;
import common.data.AutoSavers;
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
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
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
    Label timeIntervalLabel, hidingTimeLabel;

    @FXML
    Slider timeIntervalSlider, hidingTimeSlider;

    @FXML
    Button snellenButton, cButton, stdLogButton, etdrsButton;

    @FXML
    ComboBox<ScoreCounting> scoreCountingBox;

    @FXML
    CheckBox leftEyeBox, rightEyeBox, dualEyesBox;

    @FXML
    ComboBox<Double> distanceBox;

    private ResourceBundle bundle;
    private Stage thisStage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.bundle = resourceBundle;

        setFrameTimeSliderListener();
        setCheckBoxListeners();

        refreshTimeIntervalLabel(timeIntervalSlider.getValue());
        refreshHidingTimeLabel(hidingTimeSlider.getValue());

        scoreCountingBox.getItems().addAll(
                ScoreCounting.FIVE,
                ScoreCounting.DEC,
                ScoreCounting.LOG_MAR,
                ScoreCounting.FRAC_METER);

        setScoreCountingListener();
        restoreFromCache();

        setTestButtons();

        distanceBox.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            AutoSavers.getCacheSaver().putCache(CacheSaver.TEST_DISTANCE, newValue);
        }));
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
        connectionView.setup(stage, this);

        stage.show();
    }

    @FXML
    void onDisconnectClicked() throws IOException {
        ClientManager.closeAndDiscardCurrentClient();
//        ClientManager.discardCurrentClient();
        setDisconnected();
    }

    @FXML
    void snellenChartClicked() {
        showTestView(TestType.SNELLEN);
    }

    @FXML
    void cChartClicked() {
        showTestView(TestType.LANDOLT);
    }

    @FXML
    void stdLogChartClicked() {
        showTestView(TestType.STD_LOG);
    }

    @FXML
    void etdrsCharClicked() {
        showTestView(TestType.ETDRS);
    }

    @FXML
    void showHistory() {
        try {
            Stage stage = new Stage();
            stage.initOwner(thisStage);
            stage.initModality(Modality.WINDOW_MODAL);

            FXMLLoader loader =
                    new FXMLLoader(getClass().getResource("/dvaTest/fxml/historyView.fxml"),
                            bundle);
            Parent root = loader.load();

            stage.setTitle(bundle.getString("testHistory"));
            stage.setScene(new Scene(root));

            HistoryView historyView = loader.getController();
            historyView.setStage(stage);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            EventLogger.log(e);
        }
    }

    @FXML
    void showAbout() throws IOException {
        Stage stage = new Stage();

        FXMLLoader loader =
                new FXMLLoader(getClass().getResource("/dvaTest/fxml/aboutView.fxml"),
                        TestApp.getBundle());
        Parent root = loader.load();

        stage.setScene(new Scene(root));
        stage.setTitle(bundle.getString("appName"));

        stage.show();
    }

    public void setStage(Stage stage) {
        this.thisStage = stage;
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

    private long getHidingMills() {
        return (long) (hidingTimeSlider.getValue() * 1000);
    }

    private void showTestView(TestType testType) {
        TestPref testPref = new TestPref.TestPrefBuilder()
                .testType(testType)
                .scoreCounting(scoreCountingBox.getValue())
                .distance(distanceBox.getValue())
                .frameTimeMills(getTimeInterval())
                .hidingTimeMills(getHidingMills())
                .leftRightDualEyes(leftEyeBox.isSelected(), rightEyeBox.isSelected(), dualEyesBox.isSelected())
                .build();
//        storeCache();
        try {
            Stage stage = new Stage();
            stage.initOwner(thisStage);
            stage.initModality(Modality.WINDOW_MODAL);

            FXMLLoader loader =
                    new FXMLLoader(getClass().getResource("/dvaTest/fxml/testPrepView.fxml"),
                            bundle);
            Parent root = loader.load();

//            stage.setTitle(testType.show(bundle, true));
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

    private void setCheckBoxListeners() {
        leftEyeBox.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            setTestButtons();
            AutoSavers.getCacheSaver().putCache(CacheSaver.TEST_LEFT_EYE, newValue);
        }));
        rightEyeBox.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            setTestButtons();
            AutoSavers.getCacheSaver().putCache(CacheSaver.TEST_RIGHT_EYE, newValue);
        }));
        dualEyesBox.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            setTestButtons();
            AutoSavers.getCacheSaver().putCache(CacheSaver.TEST_DUAL_EYES, newValue);
        }));
    }

    private void setTestButtons() {
        if (leftEyeBox.isSelected() || rightEyeBox.isSelected() || dualEyesBox.isSelected()) {
            setTestButtonsByScoreCounting(scoreCountingBox.getValue().isLogMar);
        } else {
            snellenButton.setDisable(true);
            cButton.setDisable(true);
            stdLogButton.setDisable(true);
            etdrsButton.setDisable(true);
        }
    }

    private void setFrameTimeSliderListener() {
        timeIntervalSlider.valueProperty().addListener(((observableValue, number, t1) -> {
            refreshTimeIntervalLabel(t1.doubleValue());
            AutoSavers.getCacheSaver().putCache(CacheSaver.TEST_INTERVAL,
                    (long) (t1.doubleValue() * 1000));
        }));
        hidingTimeSlider.valueProperty().addListener(((observableValue, number, t1) -> {
            refreshHidingTimeLabel(t1.doubleValue());
            AutoSavers.getCacheSaver().putCache(CacheSaver.TEST_HIDING_INTERVAL,
                    (long) (t1.doubleValue() * 1000));
        }));
    }

    private void setScoreCountingListener() {
        scoreCountingBox.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            setTestButtonsByScoreCounting(newValue.isLogMar);
            AutoSavers.getCacheSaver().putCache(CacheSaver.TEST_SCORE_COUNTING, newValue.name());
        }));
    }

    private void setTestButtonsByScoreCounting(boolean isLogMar) {
        if (isLogMar) {
            snellenButton.setDisable(true);
            cButton.setDisable(false);
            stdLogButton.setDisable(false);
            etdrsButton.setDisable(false);
        } else {
            snellenButton.setDisable(false);
            cButton.setDisable(true);
            stdLogButton.setDisable(true);
            etdrsButton.setDisable(true);
        }
    }

    private void refreshTimeIntervalLabel(double value) {
        int d = (int) value;
        double frac = value - d;
        double resFrac = Math.round(frac * 10 / 5) * 5;
        String res = String.format("%.1f", resFrac / 10 + d);

        timeIntervalLabel.setText(res);
    }

    private void refreshHidingTimeLabel(double value) {
        int d = (int) value;
        double frac = value - d;
        double resFrac = Math.round(frac * 10 / 5) * 5;
        String res = String.format("%.1f", resFrac / 10 + d);

        hidingTimeLabel.setText(res);
    }

    private void restoreFromCache() {
        CacheSaver cacheSaver = AutoSavers.getCacheSaver();
        CacheSaver.MainViewCache mvc = cacheSaver.getMainViewCache();
        scoreCountingBox.getSelectionModel().select(mvc.scoreCounting);
        timeIntervalSlider.setValue((double) mvc.timeInterval / 1000);
        hidingTimeSlider.setValue((double) mvc.hidingInterval / 1000);
        distanceBox.getSelectionModel().select(mvc.testDistance);
        leftEyeBox.setSelected(mvc.leftEye);
        rightEyeBox.setSelected(mvc.rightEye);
        dualEyesBox.setSelected(mvc.dualEyes);
    }
}
