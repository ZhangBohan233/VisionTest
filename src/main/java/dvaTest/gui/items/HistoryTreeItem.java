package dvaTest.gui.items;

import dvaTest.TestApp;
import dvaTest.testCore.ResultRecord;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TreeItem;

import java.io.File;
import java.util.List;

public class HistoryTreeItem extends TreeItem<HistoryTreeItem.Item> {

    public HistoryTreeItem(Item item) {
        super(item);
        item.setParent(this);
    }

    public void exportToList(List<ResultRecord.NamedRecord> recordList) {
        getValue().exportToList(recordList);
    }

    public abstract static class Item {

        final CheckBox checkBox = new CheckBox();
        HistoryTreeItem parent;

        private void setParent(HistoryTreeItem parent) {
            this.parent = parent;
        }

        void addListedCheckBoxListener() {
            checkBox.selectedProperty().addListener(((observable, oldValue, newValue) -> {
                if (newValue) {
                    for (TreeItem<Item> child : parent.getChildren()) {
                        child.getValue().checkBox.setSelected(true);
                    }
                } else {
                    for (TreeItem<Item> child : parent.getChildren()) {
                        child.getValue().checkBox.setSelected(false);
                    }
                }
            }));
        }

        void traverseChildren(List<ResultRecord.NamedRecord> recordList) {
            for (TreeItem<Item> child : parent.getChildren()) {
                ((HistoryTreeItem) child).exportToList(recordList);
            }
        }

        @FXML
        public abstract CheckBox getCheckBox();

        @FXML
        public abstract String getTimeOrName();

        abstract void exportToList(List<ResultRecord.NamedRecord> recordList);
    }

    public static class Person extends Item {
        private final String name;

        public Person(String name) {
            this.name = name;

            addListedCheckBoxListener();
        }

        @Override
        public CheckBox getCheckBox() {
            return checkBox;
        }

        @Override
        public String getTimeOrName() {
            return name;
        }

        @Override
        void exportToList(List<ResultRecord.NamedRecord> recordList) {
            traverseChildren(recordList);
        }
    }

    public static class Test extends Item {
        public final ResultRecord.NamedRecord record;
        private final String showing;

        public Test(ResultRecord.NamedRecord record) {
            this.record = record;

            if (record.note.length() == 0) {
                showing = TestApp.getFullDateFormat().format(record.resultRecord.testStartTime);
            } else {
                String[] lines = record.note.split("\n");
                if (lines.length == 0) showing = TestApp.getFullDateFormat().format(record.resultRecord.testStartTime);
                else if (lines.length == 1) showing = lines[0];
                else showing = lines[0] + " ...";
            }
        }

        @Override
        public CheckBox getCheckBox() {
            return checkBox;
        }

        @Override
        public String getTimeOrName() {
            return showing;
        }

        @Override
        void exportToList(List<ResultRecord.NamedRecord> recordList) {
            if (checkBox.isSelected())
                recordList.add(record);
        }
    }

    public static class RootPage extends Item {

        public RootPage() {
            addListedCheckBoxListener();
        }

        @Override
        public CheckBox getCheckBox() {
            return checkBox;
        }

        @Override
        public String getTimeOrName() {
            return TestApp.getBundle().getString("testHistory");
        }

        @Override
        void exportToList(List<ResultRecord.NamedRecord> recordList) {
            traverseChildren(recordList);
        }
    }
}
