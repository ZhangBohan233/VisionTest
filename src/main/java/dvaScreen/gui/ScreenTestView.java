package dvaScreen.gui;

import dvaTest.testCore.EyeSide;
import dvaTest.testCore.tests.TestUnit;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class ScreenTestView implements Initializable {

    @FXML
    ImageView imageView;

    @FXML
    Label eyeSideLabel;

    private ResourceBundle bundle;

    private double pixelPerMm;
    private double systemZoom;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.bundle = resourceBundle;
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
        eyeSideLabel.setVisible(false);
        imageView.setVisible(true);

        Image image = testUnit.getTestImage().getImage();
        System.out.println("Received " + testUnit.getTestImage().getName());

        final double graphHeightMm = testUnit.getGraphScale() *
                testUnit.getTestType().getTest().standardHeightMm(testUnit.getDistance());
        final double graphHeightPixels = Math.round(graphHeightMm * pixelPerMm / systemZoom);

        Platform.runLater(() -> {
            // 显示图片
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
            // 显示空白
            imageView.setImage(null);
            System.out.println("Blank");
        });
    }

    public void showSideText(EyeSide eyeSide) {
        Platform.runLater(() -> {
            imageView.setVisible(false);
            eyeSideLabel.setVisible(true);
            eyeSideLabel.setText(eyeSide.toString());
        });
    }
}
