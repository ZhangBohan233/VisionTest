package dvaTest.gui;

import common.data.DataSaver;
import dvaTest.TestApp;
import dvaTest.gui.items.HistoryTreeItem;
import dvaTest.gui.widgets.ResultPane;
import dvaTest.testCore.ResultRecord;
import dvaTest.testCore.TestPref;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class HistoryView implements Initializable {

    @FXML
    TreeTableView<HistoryTreeItem.Item> historyTable;

    @FXML
    TreeTableColumn<HistoryTreeItem.Item, String> subjectCol;

    @FXML
    TreeTableColumn<HistoryTreeItem.Item, String> timeCol;

    @FXML
    Pane rightPane;

    @FXML
    Label testTypeLabel, scoreCountingLabel, distanceLabel, showingTimeLabel, hidingTimeLabel, testTimeLabel;

    private ResourceBundle bundle;

    private Stage stage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.bundle = resourceBundle;

        fillTable();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private void fillTable() {
        subjectCol.setCellValueFactory(p -> {
            HistoryTreeItem.Item hi = p.getValue().getValue();
            return new ReadOnlyStringWrapper(hi.getSubject());
        });
//        timeCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<HistoryItem, String> p) -> {
        timeCol.setCellValueFactory(p -> {
            HistoryTreeItem.Item hi = p.getValue().getValue();
            return new ReadOnlyStringWrapper(hi.getTime());
        });

        historyTable.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue.getValue() instanceof HistoryTreeItem.Test) {
                showRightPane((HistoryTreeItem.Test) newValue.getValue());
            } else {
                hideRightPane();
            }
        }));

        historyTable.setRoot(readTestToTable());
    }

    private TreeItem<HistoryTreeItem.Item> readTestToTable() {
        TreeItem<HistoryTreeItem.Item> rootItem = new TreeItem<>(new HistoryTreeItem.RootPage());
        rootItem.setExpanded(true);
        File dataDir = new File(DataSaver.DATA_DIR);
        if (dataDir.exists()) {
            File[] people = dataDir.listFiles();
            if (people == null || people.length == 0)
                return null;
            else {
                for (File personDir : people) {
                    HistoryTreeItem personItem =
                            new HistoryTreeItem(new HistoryTreeItem.Person(personDir.getName()));
                    rootItem.getChildren().add(personItem);

                    File[] personRec = personDir.listFiles();
                    if (personRec != null) {
                        for (File rec : personRec) {
                            ResultRecord.NamedRecord record = DataSaver.loadSavedResult(rec);
                            if (record != null) {
                                personItem.getChildren().add(
                                        new HistoryTreeItem(new HistoryTreeItem.Test(rec, record)));
                            }
                        }
                    }
                }
            }
        }
        return rootItem;
    }

    private void showRightPane(HistoryTreeItem.Test test) {
        if (!rightPane.getChildren().isEmpty()) {
            int indexOfLast = rightPane.getChildren().size() - 1;
            if (rightPane.getChildren().get(indexOfLast) instanceof ResultPane) {
                rightPane.getChildren().remove(indexOfLast);
            }
        }

        ResultPane rp = new ResultPane();
        rp.setup(test.record.resultRecord);
        rightPane.setManaged(true);
        rightPane.setVisible(true);
        rightPane.getChildren().add(rp);

        TestPref testPref = test.record.resultRecord.testPref;

        testTypeLabel.setText(testPref.getTestType().show(bundle));
        scoreCountingLabel.setText(testPref.getScoreCounting().toString());
        showingTimeLabel.setText((double) testPref.getIntervalMills() / 1000 + " " + bundle.getString("unitSecond"));
        distanceLabel.setText(testPref.getDistance() + " " + bundle.getString("unitMeters"));
        testTimeLabel.setText(TestApp.getDateFormat().format(test.record.creationTime));

        stage.sizeToScene();
    }

    private void hideRightPane() {
        rightPane.setManaged(false);
        rightPane.setVisible(false);
        rightPane.getChildren().clear();
    }
}
