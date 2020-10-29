package dvaTest.gui.items;

import dvaTest.TestApp;
import dvaTest.testCore.ResultRecord;
import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HistoryTreeItem extends TreeItem<HistoryTreeItem.Item> {

    public HistoryTreeItem(Item item) {
        super(item);
    }

    public abstract static class Item {

        @FXML
        public abstract String getSubject();

        @FXML
        public abstract String getTime();
    }

    public static class Person extends Item {
        private final String name;

        public Person(String name) {
            this.name = name;
        }

        @Override
        public String getSubject() {
            return name;
        }

        @Override
        public String getTime() {
            return "";
        }
    }

    public static class Test extends Item {
        private final File file;
        public final ResultRecord.NamedRecord record;

        public Test(File file, ResultRecord.NamedRecord record) {
            this.file = file;
            this.record = record;
        }

        @Override
        public String getSubject() {
            return file.getName();
        }

        @Override
        public String getTime() {
            return TestApp.getDateFormat().format(record.creationTime);
        }
    }

    public static class RootPage extends Item {

        @Override
        public String getSubject() {
            return TestApp.getBundle().getString("testHistory");
        }

        @Override
        public String getTime() {
            return "";
        }
    }
}
