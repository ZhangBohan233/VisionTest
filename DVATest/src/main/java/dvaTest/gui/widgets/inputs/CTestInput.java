package dvaTest.gui.widgets.inputs;

import dvaTest.Main;
import dvaTest.gui.TestView;
import dvaTest.testCore.testItems.CTestImage;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.util.ResourceBundle;

public class CTestInput extends TestInput {

    static final ResourceBundle bundle = Main.getBundle();

    public CTestInput() {
        super();

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
        directionButtonClicked("upLeft");
    }

    @FXML
    void upClicked() {
        directionButtonClicked("up");
    }

    @FXML
    void upRightClicked() {
        directionButtonClicked("upRight");
    }

    @FXML
    void leftClicked() {
        directionButtonClicked("left");
    }

    @FXML
    void rightClicked() {
        directionButtonClicked("right");
    }

    @FXML
    void downLeftClicked() {
        directionButtonClicked("downLeft");
    }

    @FXML
    void downClicked() {
        directionButtonClicked("down");
    }

    @FXML
    void downRightClicked() {
        directionButtonClicked("downRight");
    }

    private void directionButtonClicked(String directionName) {
        testController.userInput(directionName);
    }
}
