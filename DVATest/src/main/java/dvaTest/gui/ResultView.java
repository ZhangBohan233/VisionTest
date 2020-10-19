package dvaTest.gui;

import dvaScreen.gui.items.ResolutionItem;
import dvaTest.gui.items.ResultTableItem;
import dvaTest.testCore.TestResultUnit;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.*;

public class ResultView implements Initializable {

    @FXML
    TableView<ResultTableItem> resultTable;

    @FXML
    TableColumn<ResultTableItem, Double> visionLevelCol;

    @FXML
    TableColumn<ResolutionItem, Integer> correctCountCol, incorrectCountCol;

    @FXML
    TableColumn<ResolutionItem, String> correctRatioCol;

    private ResourceBundle bundle;
    private Stage thisStage;

    private List<TestResultUnit> resultUnitList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.bundle = resourceBundle;

        setTableFactory();
    }

    public void setup(Stage stage, List<TestResultUnit> resultUnitList) {
        this.thisStage = stage;
        this.resultUnitList = resultUnitList;

        Map<Double, int[]> sucFailMap = new TreeMap<>();  // vision level: [correct, incorrect]
        for (TestResultUnit tru: resultUnitList) {
            double visionLevel = tru.getTestUnit().getVisionLevel();
            int[] res = sucFailMap.computeIfAbsent(visionLevel, k -> new int[2]);
            if (tru.isCorrect()) res[0]++;
            else res[1]++;
        }

        for (Map.Entry<Double, int[]> entry : sucFailMap.entrySet()) {
            resultTable.getItems().add(new ResultTableItem(entry.getKey(), entry.getValue()[0], entry.getValue()[1]));
        }

        Collections.sort(resultTable.getItems());
    }

    private void setTableFactory() {
        visionLevelCol.setCellValueFactory(new PropertyValueFactory<>("visionLevel"));
        correctCountCol.setCellValueFactory(new PropertyValueFactory<>("correctCount"));
        incorrectCountCol.setCellValueFactory(new PropertyValueFactory<>("incorrectCount"));
        correctRatioCol.setCellValueFactory(new PropertyValueFactory<>("correctRatio"));
    }
}
