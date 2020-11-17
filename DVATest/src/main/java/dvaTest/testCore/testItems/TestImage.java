package dvaTest.testCore.testItems;

import dvaTest.testCore.TestType;
import dvaTest.testCore.tests.CTest;
import dvaTest.testCore.tests.EtdrsTest;
import dvaTest.testCore.tests.SnellenTest;
import dvaTest.testCore.tests.StdLogTest;

import java.util.Map;

public class TestImage {

    public final static String IMAGE_RESOURCE_DIR = "/common/images/";
    private final String name;
    private final String imagePath;

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

    public String getImagePath() {
        return imagePath;
    }

    public String getName() {
        return name;
    }
}
