module DVATest {
    requires javafx.base;
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.fxml;

    exports dvaTest;
    exports dvaTest.gui;

    opens dvaTest.gui;
}