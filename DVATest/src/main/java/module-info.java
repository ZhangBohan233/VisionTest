module DVATest {
    requires javafx.base;
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.fxml;
    requires org.json;
    requires java.desktop;

    exports dvaTest;
    exports dvaTest.gui;
    exports dvaTest.gui.widgets;
    exports dvaTest.gui.widgets.inputs;
    exports dvaTest.gui.items;
    exports dvaTest.testCore;
    exports dvaTest.testCore.testItems;
    exports dvaTest.testCore.tests;

    exports dvaScreen;
    exports dvaScreen.gui;
    exports dvaScreen.gui.items;

    opens dvaScreen.gui;
    opens dvaTest.gui;
    opens dvaTest.gui.widgets;
    opens dvaTest.gui.widgets.inputs;
}