package dvaTest.gui;

import common.EventLogger;
import common.data.DataSaver;
import dvaTest.TestApp;
import dvaTest.gui.items.HistoryTreeItem;
import dvaTest.gui.widgets.ResultPane;
import dvaTest.testCore.ResultRecord;
import dvaTest.testCore.TestPref;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class HistoryView implements Initializable {

    @FXML
    TreeTableView<HistoryTreeItem.Item> historyTable;

    @FXML
    TreeTableColumn<HistoryTreeItem.Item, CheckBox> checkBoxCol;

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

    @FXML
    void exportAction() {
        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter xlsxFilter =
                new FileChooser.ExtensionFilter(bundle.getString("xlsx"), "*.xlsx");

        fileChooser.getExtensionFilters().addAll(xlsxFilter);

        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            FileChooser.ExtensionFilter extensionFilter = fileChooser.getSelectedExtensionFilter();
            String extension = extensionFilter.getExtensions().get(0);
            List<ResultRecord.NamedRecord> recordList = getAllSelected();
            if (extension.equals("*.xlsx")) {
                try {
                    DataSaver.exportAsXlsx(file, recordList);
                    AlertShower.showSuccess(bundle.getString("exportSuccess"), bundle, stage);
                } catch (IOException e) {
                    AlertShower.showError(bundle.getString("exportFailed"), bundle, stage);
                    EventLogger.log(e);
                }
            }
        }
    }

    @FXML
    void deleteAction() {

    }

    private List<ResultRecord.NamedRecord> getAllSelected() {
        List<ResultRecord.NamedRecord> recordList = new ArrayList<>();
        ((HistoryTreeItem) historyTable.getRoot()).exportToList(recordList);
        return recordList;
    }

    private void fillTable() {
        subjectCol.setCellValueFactory(p -> {
            HistoryTreeItem.Item hi = p.getValue().getValue();
            return new ReadOnlyStringWrapper(hi.getSubject());
        });
        timeCol.setCellValueFactory(p -> {
            HistoryTreeItem.Item hi = p.getValue().getValue();
            return new ReadOnlyStringWrapper(hi.getTime());
        });
        checkBoxCol.setCellValueFactory(p -> {
            HistoryTreeItem.Item hi = p.getValue().getValue();
            return new ReadOnlyObjectWrapper<>(hi.getCheckBox());
        });
        historyTable.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue.getValue() instanceof HistoryTreeItem.Test) {
                showRightPane((HistoryTreeItem.Test) newValue.getValue());
            } else {
                hideRightPane();
            }
        }));

        HistoryTreeItem rootItem = readTestToTable();
//        rootItem.setIndependent(false);
        historyTable.setRoot(rootItem);
        historyTable.setShowRoot(true);
//        historyTable.getSelectionModel().selectedItemProperty().addListener(
//                (observable, oldValue, newValue) -> System.out.println(12321));
    }

    private HistoryTreeItem readTestToTable() {
        HistoryTreeItem rootItem = new HistoryTreeItem(new HistoryTreeItem.RootPage());
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

        testTypeLabel.setText(testPref.getTestType().show(bundle, false));
        scoreCountingLabel.setText(testPref.getScoreCounting().toString());
        showingTimeLabel.setText((double) testPref.getIntervalMills() / 1000 + " " + bundle.getString("unitSecond"));
        hidingTimeLabel.setText((double) testPref.getHidingMills() / 1000 + " " + bundle.getString("unitSecond"));
        distanceLabel.setText(testPref.getDistance() + " " + bundle.getString("unitMeters"));
        testTimeLabel.setText(TestApp.getFullDateFormat().format(test.record.creationTime));

        stage.sizeToScene();
    }

    private void hideRightPane() {
        rightPane.setManaged(false);
        rightPane.setVisible(false);
        rightPane.getChildren().clear();
    }
}
