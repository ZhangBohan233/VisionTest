module DVATest {
    requires javafx.base;
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.fxml;

    exports dvaTest;
    exports dvaTest.gui;
    exports dvaTest.gui.widgets;
    exports dvaScreen;
    exports dvaScreen.gui;

    opens dvaScreen.gui;
    opens dvaTest.gui;
    opens dvaTest.gui.widgets;
}