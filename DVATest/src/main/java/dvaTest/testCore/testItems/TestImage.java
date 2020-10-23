package dvaTest.testCore.testItems;

import dvaTest.testCore.TestType;
import dvaTest.testCore.tests.CTest;
import dvaTest.testCore.tests.StdLogTest;

import java.util.Map;

public class TestImage {

    public final static String IMAGE_RESOURCE_DIR = "/common/images/";
    private final String name;
    private final String imagePath;
    private final TestType testType;

    public TestImage(String name, String imagePath, TestType testType) {
        this.name = name;
        this.imagePath = IMAGE_RESOURCE_DIR + imagePath;
        this.testType = testType;
//        System.out.println(testType);
    }

    public static TestImage getByName(TestType testType, String name) {
        Map<String, TestImage> testItems;
        // TODO: test type
        if (testType == TestType.SNELLEN_CHART) {
            testItems = null;
        } else if (testType == TestType.C_CHART) {
            testItems = CTest.ITEMS;
        } else if (testType == TestType.E_CHART) {
            testItems = null;
        } else if (testType == TestType.STD_LOG_CHART) {
            testItems = StdLogTest.ITEMS;
        } else {
            throw new RuntimeException("No such test type. ");
        }

        return testItems.get(name);
    }

    public TestType getTestType() {
        return testType;
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
