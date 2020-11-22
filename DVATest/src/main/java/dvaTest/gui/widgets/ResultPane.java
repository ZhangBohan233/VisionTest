package dvaTest.gui.widgets;

import dvaScreen.gui.items.ResolutionItem;
import dvaTest.TestApp;
import dvaTest.gui.items.ResultTableItem;
import dvaTest.testCore.ResultRecord;
import dvaTest.testCore.TestPref;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.ResourceBundle;

public class ResultPane extends VBox {

    @FXML
    TableView<ResultTableItem> resultTable;

    @FXML
    TableColumn<ResultTableItem, Double> visionLevelCol;

    @FXML
    TableColumn<ResolutionItem, Integer> correctCountCol, incorrectCountCol;

    @FXML
    TableColumn<ResolutionItem, String> correctRatioCol;

    @FXML
    Label testTypeLabel, scoreCountingLabel, distanceLabel, showingTimeLabel,
            hidingTimeLabel, testTimeLabel, conclusionLabel;

    private ResultRecord resultRecord;

    public ResultPane() {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/dvaTest/fxml/widgets/resultPane.fxml"),
                TestApp.getBundle());
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException("Failed to generate view", e);
        }

        setTableFactory();
    }

    public void setup(ResultRecord resultRecord) {
        this.resultRecord = resultRecord;

        Map<String, int[]> sucFailMap = resultRecord.toLevelMap();

        for (Map.Entry<String, int[]> entry : sucFailMap.entrySet()) {
            resultTable.getItems().add(new ResultTableItem(entry.getKey(), entry.getValue()[0], entry.getValue()[1]));
        }

        Collections.sort(resultTable.getItems());

        ResourceBundle bundle = TestApp.getBundle();
        TestPref testPref = resultRecord.testPref;

        testTypeLabel.setText(testPref.getTestType().show(bundle, false));
        scoreCountingLabel.setText(testPref.getScoreCounting().toString());
        showingTimeLabel.setText((double) testPref.getIntervalMills() / 1000 + " " + bundle.getString("unitSecond"));
        hidingTimeLabel.setText((double) testPref.getHidingMills() / 1000 + " " + bundle.getString("unitSecond"));
        distanceLabel.setText(testPref.getDistance() + " " + bundle.getString("unitMeters"));
        testTimeLabel.setText(TestApp.getFullDateFormat().format(resultRecord.testStartTime));
        conclusionLabel.setText(resultRecord.generateScoreConclusion());
    }

    private void setTableFactory() {
        visionLevelCol.setCellValueFactory(new PropertyValueFactory<>("visionLevel"));
        correctCountCol.setCellValueFactory(new PropertyValueFactory<>("correctCount"));
        incorrectCountCol.setCellValueFactory(new PropertyValueFactory<>("incorrectCount"));
        correctRatioCol.setCellValueFactory(new PropertyValueFactory<>("correctRatio"));
    }

    public ResultRecord getResultRecord() {
        return resultRecord;
    }
}
