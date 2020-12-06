package dvaTest.testCore.testItems;

import javafx.scene.image.Image;

import java.io.IOException;
import java.io.InputStream;

public class TestImage {

    public final static String IMAGE_RESOURCE_DIR = "/common/images/";
    private final String name;
    private final String imagePath;
    private Image image;

    public TestImage(String name, String imagePath) {
        this.name = name;
        this.imagePath = IMAGE_RESOURCE_DIR + imagePath;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj.getClass() == getClass() && name.equals(((TestImage) obj).name);
    }

    public Image getImage() {
        if (image == null) {
            InputStream is = getClass().getResourceAsStream(this.imagePath);
            this.image = new Image(is);
            try {
                is.close();
                return image;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else return image;
    }

    public String getName() {
        return name;
    }
}
