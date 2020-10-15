package dvaTest.gui.widgets.inputs;

import dvaTest.Main;
import dvaTest.testCore.testItems.CTestImage;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.util.ResourceBundle;

public class CTestInput extends TestInput {

    static final ResourceBundle bundle = Main.getBundle();

    public CTestInput() {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/dvaTest/fxml/widgets/inputs/cTestInput.fxml"),
                bundle);
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException("Failed to generate view", e);
        }
    }

    @FXML
    void upLeftClicked() {
        directionButtonClicked(CTestImage.UP_LEFT);
    }

    @FXML
    void upClicked() {
        directionButtonClicked(CTestImage.UP);
    }

    @FXML
    void upRightClicked() {
        directionButtonClicked(CTestImage.UP_RIGHT);
    }

    @FXML
    void leftClicked() {
        directionButtonClicked(CTestImage.LEFT);
    }

    @FXML
    void rightClicked() {
        directionButtonClicked(CTestImage.RIGHT);
    }

    @FXML
    void downLeftClicked() {
        directionButtonClicked(CTestImage.DOWN_LEFT);
    }

    @FXML
    void downClicked() {
        directionButtonClicked(CTestImage.DOWN);
    }

    @FXML
    void downRightClicked() {
        directionButtonClicked(CTestImage.DOWN_RIGHT);
    }

    private void directionButtonClicked(int direction) {

    }
}
