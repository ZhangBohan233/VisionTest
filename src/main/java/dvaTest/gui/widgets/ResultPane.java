package dvaTest.gui.widgets;

import dvaTest.TestApp;
import dvaTest.gui.items.ResultTableItem;
import dvaTest.testCore.EyeSide;
import dvaTest.testCore.ResultRecord;
import dvaTest.testCore.TestPref;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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

//    @FXML
//    TableColumn<ResultTableItem, Double> visionLevelCol;
//
//    @FXML
//    TableColumn<ResolutionItem, Integer> correctCountCol, incorrectCountCol;
//
//    @FXML
//    TableColumn<ResolutionItem, String> correctRatioCol;

    @FXML
    Label testTypeLabel, scoreCountingLabel, distanceLabel, showingTimeLabel,
            hidingTimeLabel, testTimeLabel, noteLabel, noteContentLabel;

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

    private void addResultsToTable(TableView<ResultTableItem> tableView, Map<String, int[]> sucFailMap) {
        for (Map.Entry<String, int[]> entry: sucFailMap.entrySet()) {
            tableView.getItems().add(new ResultTableItem(entry.getKey(), entry.getValue()[0], entry.getValue()[1]));
        }
        Collections.sort(tableView.getItems());
    }

    public void setup(ResultRecord resultRecord) {
        this.resultRecord = resultRecord;

        Map<EyeSide, Map<String, int[]>> sucFailMap = resultRecord.toLevelMap();

        for (Map.Entry<EyeSide, Map<String, int[]>> entry : sucFailMap.entrySet()) {
            if (entry.getKey() == EyeSide.LEFT) {
                leftBox.setManaged(true);
                leftBox.setVisible(true);
                addResultsToTable(leftTable, entry.getValue());
                leftScoreLabel.setText(resultRecord.scoreConclusions.get(EyeSide.LEFT));
            } else if (entry.getKey() == EyeSide.RIGHT) {
                rightBox.setManaged(true);
                rightBox.setVisible(true);
                addResultsToTable(rightTable, entry.getValue());
                rightScoreLabel.setText(resultRecord.scoreConclusions.get(EyeSide.RIGHT));
            } else if (entry.getKey() == EyeSide.BOTH) {
                bothBox.setManaged(true);
                bothBox.setVisible(true);
                addResultsToTable(bothTable, entry.getValue());
                bothScoreLabel.setText(resultRecord.scoreConclusions.get(EyeSide.BOTH));
            }
        }

//        Collections.sort(resultTable.getItems());

        ResourceBundle bundle = TestApp.getBundle();
        TestPref testPref = resultRecord.testPref;

        testTypeLabel.setText(testPref.getTestType().show(bundle, false));
        scoreCountingLabel.setText(testPref.getScoreCounting().toString());
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
