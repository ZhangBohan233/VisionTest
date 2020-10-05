package dvaScreen.gui;

import dvaTest.testCore.tests.TestUnit;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class ScreenTestView implements Initializable {

    @FXML
    ImageView imageView;

    private ResourceBundle bundle;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.bundle = resourceBundle;
    }

    public void showGraph(TestUnit testUnit) {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(testUnit.getTestItem().getImagePath());
        System.out.println(getClass().getClassLoader().getResource(testUnit.getTestItem().getImagePath()));
        Image image = new Image(inputStream);

        Platform.runLater(() -> {
            imageView.setImage(image);
        });

        try {
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
