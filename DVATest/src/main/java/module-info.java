module DVATest {
    requires javafx.base;
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.fxml;

    exports dvaTest;
    exports dvaTest.gui;
    exports dvaTest.gui.widgets;
    exports dvaTest.gui.widgets.inputs;
    exports dvaScreen;
    exports dvaScreen.gui;
    exports dvaTest.testCore;
    exports dvaTest.testCore.testItems;
    exports dvaTest.testCore.tests;

    opens dvaScreen.gui;
    opens dvaTest.gui;
    opens dvaTest.gui.widgets;
    opens dvaTest.gui.widgets.inputs;
}