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

    private double pixelPerMm;
    private double imageHeight;
    private double systemZoom;

    private Image blankImage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.bundle = resourceBundle;

        InputStream inputStream1 = getClass().getResourceAsStream("/common/images/c/C100.png");
        Image image = new Image(inputStream1);
        imageView.setFitHeight(500);
        imageView.setImage(image);

        InputStream inputStream2 = getClass().getResourceAsStream("/common/images/c/C_BLANK.jpg");
        blankImage = new Image(inputStream2);

        try {
            inputStream1.close();
            inputStream2.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param pixelPerMm 每毫米屏幕（短边）包含的像素行数
     * @param systemZoom 系统缩放
     */
    public void setScreenParams(double pixelPerMm, double systemZoom) {
        this.pixelPerMm = pixelPerMm;
        this.systemZoom = systemZoom;
    }

    public void showGraph(TestUnit testUnit) {
        Image image = testUnit.getTestImage().getImage();
        System.out.println("Received " + testUnit.getTestImage().getName());

        final double graphHeightMm = testUnit.getGraphScale() *
                testUnit.getTestType().getTest().standardHeightMm(testUnit.getDistance());
        final double graphHeightPixels = Math.round(graphHeightMm * pixelPerMm / systemZoom);

        Platform.runLater(() -> {
            imageView.setFitHeight(graphHeightPixels);
            imageView.setImage(image);
            System.out.println("Shown " + testUnit.getTestImage().getName() + " " + testUnit.getVisionLevel());
        });

        try {
            Thread.sleep(testUnit.getTimeInterval());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Platform.runLater(() -> {
            imageView.setImage(blankImage);
            System.out.println("Blank");
        });
    }
}
