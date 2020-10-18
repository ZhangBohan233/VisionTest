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

    private double ppi;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.bundle = resourceBundle;

        InputStream inputStream = getClass().getResourceAsStream("/common/images/c/C_BLANK.png");
        Image image = new Image(inputStream);
        imageView.setFitWidth(500.0);
        imageView.setImage(image);

        try {
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setPpi(double ppi) {
        this.ppi = ppi;
    }

    public void showGraph(TestUnit testUnit) {
        InputStream inputStream = getClass().getResourceAsStream(testUnit.getTestItem().getImagePath());
        System.out.println("Received " + testUnit.getTestItem().getImagePath());
        Image image = new Image(inputStream);

        Platform.runLater(() -> {
            imageView.setFitWidth(500.0);
            imageView.setImage(image);
            System.out.println("Shown " + testUnit.getTestItem().getImagePath());
        });

        try {
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
