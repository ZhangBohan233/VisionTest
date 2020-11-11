package dvaTest.gui.widgets.inputs;

import dvaTest.TestApp;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;

import java.io.IOException;
import java.util.ResourceBundle;

public class EtdrsTestInput extends TestInput {

    static final ResourceBundle bundle = TestApp.getBundle();

    public EtdrsTestInput() {
        super();

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/dvaTest/fxml/widgets/inputs/etdrsTestInput.fxml"),
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
    void buttonClicked(ActionEvent event) {
        String text = ((Button) event.getSource()).getText();
        textButtonClicked(text);
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        textButtonClicked(keyEvent.getText().toUpperCase());
    }

    private void textButtonClicked(String buttonText) {
        testController.userInput(buttonText, buttonText);
    }
}
