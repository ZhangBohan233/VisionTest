package dvaTest.gui.widgets.inputs;

import dvaTest.TestApp;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.io.IOException;
import java.util.ResourceBundle;

public class CTestInput extends TestInput {

    static final ResourceBundle bundle = TestApp.getBundle();

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
    void upLeftClicked(ActionEvent event) {
        directionButtonClicked("upLeft", ((Button) event.getSource()).getText());
    }

    @FXML
    void upClicked(ActionEvent event) {
        directionButtonClicked("up", ((Button) event.getSource()).getText());
    }

    @FXML
    void upRightClicked(ActionEvent event) {
        directionButtonClicked("upRight", ((Button) event.getSource()).getText());
    }

    @FXML
    void leftClicked(ActionEvent event) {
        directionButtonClicked("left", ((Button) event.getSource()).getText());
    }

    @FXML
    void rightClicked(ActionEvent event) {
        directionButtonClicked("right", ((Button) event.getSource()).getText());
    }

    @FXML
    void downLeftClicked(ActionEvent event) {
        directionButtonClicked("downLeft", ((Button) event.getSource()).getText());
    }

    @FXML
    void downClicked(ActionEvent event) {
        directionButtonClicked("down", ((Button) event.getSource()).getText());
    }

    @FXML
    void downRightClicked(ActionEvent event) {
        directionButtonClicked("downRight", ((Button) event.getSource()).getText());
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.NUMPAD8) {
            directionButtonClicked("up", "↑");
        } else if (keyEvent.getCode() == KeyCode.NUMPAD7) {
            directionButtonClicked("upLeft", "↖");
        } else if (keyEvent.getCode() == KeyCode.NUMPAD4) {
            directionButtonClicked("left", "←");
        } else if (keyEvent.getCode() == KeyCode.NUMPAD1) {
            directionButtonClicked("downLeft", "↙");
        } else if (keyEvent.getCode() == KeyCode.NUMPAD2) {
            directionButtonClicked("down", "↓");
        } else if (keyEvent.getCode() == KeyCode.NUMPAD3) {
            directionButtonClicked("downRight", "↘");
        } else if (keyEvent.getCode() == KeyCode.NUMPAD6) {
            directionButtonClicked("right", "→");
        } else if (keyEvent.getCode() == KeyCode.NUMPAD9) {
            directionButtonClicked("upRight", "↗");
        }
    }

    private void directionButtonClicked(String directionName, String buttonText) {
        testController.userInput(directionName, buttonText);
    }
}
