package dvaTest.testCore.testItems;

import dvaTest.testCore.TestType;

import java.util.List;

public abstract class TestImage {

    public final static String IMAGE_RESOURCE_DIR = "/common/images/";
    protected final byte ordNum;
    protected final String description;
    protected final String imagePath;

    TestImage(int ordNum, String description, String imagePath) {
        this.ordNum = (byte) ordNum;
        this.description = description;
        this.imagePath = IMAGE_RESOURCE_DIR + imagePath;
    }

    public abstract TestType testType();

    public static TestImage getByOrdNum(TestType testType, byte ordNum) {
        List<TestImage> testItems;
        if (testType == TestType.SNELLEN_CHART) {
            testItems = null;
        } else if (testType == TestType.C_CHART) {
            testItems = CTestImage.ITEMS;
        } else if (testType == TestType.E_CHART) {
            testItems = null;
        } else {
            throw new RuntimeException("No such test type. ");
        }

        return testItems.get((ordNum & 0xff) - 1);
    }

    public byte getOrdNum() {
        return ordNum;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getDescription() {
        return description;
    }
}
