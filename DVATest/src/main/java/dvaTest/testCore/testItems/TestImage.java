package dvaTest.testCore.testItems;

import dvaTest.testCore.TestType;
import dvaTest.testCore.tests.CTest;

import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class TestImage {

    public final static String IMAGE_RESOURCE_DIR = "/common/images/";
    protected final String name;
    protected final String imagePath;

    TestImage(String name, String imagePath) {
        this.name = name;
        this.imagePath = IMAGE_RESOURCE_DIR + imagePath;
    }

    public abstract TestType testType();

    public static TestImage getByName(TestType testType, String name) {
        Map<String, TestImage> testItems;
        if (testType == TestType.SNELLEN_CHART) {
            testItems = null;
        } else if (testType == TestType.C_CHART) {
            testItems = CTest.ITEMS;
        } else if (testType == TestType.E_CHART) {
            testItems = null;
        } else {
            throw new RuntimeException("No such test type. ");
        }

        return testItems.get(name);
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
