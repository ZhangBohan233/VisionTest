package dvaTest.gui.widgets.inputs;

import dvaTest.TestApp;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;

import java.io.IOException;
import java.util.ResourceBundle;

public class StdLogTestInput extends TestInput {

    static final ResourceBundle bundle = TestApp.getBundle();

    public StdLogTestInput() {
        super();

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/dvaTest/fxml/widgets/inputs/stdLogTestInput.fxml"),
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
    void upClicked(ActionEvent event) {
        directionButtonClicked("up", ((Button) event.getSource()).getText());
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
    void downClicked(ActionEvent event) {
        directionButtonClicked("down", ((Button) event.getSource()).getText());
    }

    private void directionButtonClicked(String directionName, String buttonText) {
        testController.userInput(directionName, buttonText);
    }
}
