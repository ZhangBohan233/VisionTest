package dvaTest.gui.widgets;

import dvaTest.TestApp;
import dvaTest.gui.items.ResultTableItem;
import dvaTest.gui.items.ScoreCounting;
import dvaTest.testCore.EyeSide;
import dvaTest.testCore.ResultRecord;
import dvaTest.testCore.TestPref;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.ResourceBundle;

public class ResultPane extends VBox {

    @FXML
    TableView<ResultTableItem> leftTable, rightTable, bothTable;

    @FXML
    VBox leftBox, rightBox, bothBox;

    @FXML
    Label leftScoreLabel, rightScoreLabel, bothScoreLabel;

    @FXML
    Label testTypeLabel, distanceLabel, showingTimeLabel,
            hidingTimeLabel, testTimeLabel, noteLabel, noteContentLabel;

    @FXML
    ComboBox<ScoreCounting> scoreCountingBox;

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

    private void setResultToTable(TableView<ResultTableItem> tableView, Map<String, int[]> sucFailMap) {
        tableView.getItems().clear();
        for (Map.Entry<String, int[]> entry : sucFailMap.entrySet()) {
            tableView.getItems().add(new ResultTableItem(entry.getKey(), entry.getValue()[0], entry.getValue()[1]));
        }
        Collections.sort(tableView.getItems());
    }

    public void setup(ResultRecord resultRecord) {
        this.resultRecord = resultRecord;

        ResourceBundle bundle = TestApp.getBundle();
        TestPref testPref = resultRecord.testPref;

        setTestTypeBox(testPref.getScoreCounting());

        testTypeLabel.setText(testPref.getTestType().show(bundle, false));
        showingTimeLabel.setText((double) testPref.getIntervalMills() / 1000 + " " + bundle.getString("unitSecond"));
        hidingTimeLabel.setText((double) testPref.getHidingMills() / 1000 + " " + bundle.getString("unitSecond"));
        distanceLabel.setText(testPref.getDistance() + " " + bundle.getString("unitMeters"));
        testTimeLabel.setText(TestApp.getFullDateFormat().format(resultRecord.testStartTime));
    }

    public void setNote(String note) {
        if (note != null && note.length() > 0) {
            noteLabel.setVisible(true);
            noteContentLabel.setVisible(true);
            noteContentLabel.setText(note);
        }
    }

    private void refreshScoreCounting(ScoreCounting newSc) {
        Map<EyeSide, Map<String, int[]>> sucFailMap =
                resultRecord.toLevelMap(
                        resultRecord.testPref.getTestType().getTest(),
                        resultRecord.testPref.getScoreCounting(),
                        newSc
                );

        Map<EyeSide, String> conclusion = resultRecord.generateScoreConclusions(newSc);
        for (Map.Entry<EyeSide, Map<String, int[]>> entry : sucFailMap.entrySet()) {
            if (entry.getKey() == EyeSide.LEFT) {
                leftBox.setManaged(true);
                leftBox.setVisible(true);
                setResultToTable(leftTable, entry.getValue());
                leftScoreLabel.setText(conclusion.get(EyeSide.LEFT));
            } else if (entry.getKey() == EyeSide.RIGHT) {
                rightBox.setManaged(true);
                rightBox.setVisible(true);
                setResultToTable(rightTable, entry.getValue());
                rightScoreLabel.setText(conclusion.get(EyeSide.RIGHT));
            } else if (entry.getKey() == EyeSide.BOTH) {
                bothBox.setManaged(true);
                bothBox.setVisible(true);
                setResultToTable(bothTable, entry.getValue());
                bothScoreLabel.setText(conclusion.get(EyeSide.BOTH));
            }
        }
    }

    private void setTestTypeBox(ScoreCounting scoreCounting) {
        scoreCountingBox.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue != null)
                refreshScoreCounting(newValue);
        }));

        if (scoreCounting.isLogMar) {
            scoreCountingBox.getItems().addAll(
                    ScoreCounting.FIVE, ScoreCounting.DEC, ScoreCounting.LOG_MAR
            );
        } else {
            scoreCountingBox.getItems().addAll(
                    ScoreCounting.FRAC_METER
            );
        }
        scoreCountingBox.getSelectionModel().select(scoreCounting);
    }

    @SuppressWarnings("unchecked")
    private void setTableFactory() {
        for (TableView<ResultTableItem> tv : new TableView[]{leftTable, rightTable, bothTable}) {
            tv.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("visionLevel"));
            tv.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("correctCount"));
            tv.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("incorrectCount"));
            tv.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("correctRatio"));
        }
    }

    public ResultRecord getResultRecord() {
        return resultRecord;
    }
}
