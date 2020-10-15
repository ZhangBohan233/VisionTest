package dvaTest.testCore.tests;

import dvaTest.testCore.testItems.CTestImage;
import dvaTest.testCore.testItems.TestImage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Test {

    protected List<Double> visionLevels;
    protected Map<String, TestImage> testImageMap;
    protected TestImage[] testImages;
    protected int currentLevelIndex;

    /**
     * 在当前等级的测试次数
     */
    protected int currentLevelCount;

    public Test(List<Double> visionLevels, Map<String, TestImage> testImageMap) {
        this.visionLevels = visionLevels;
        this.testImageMap = testImageMap;

        testImages = testImageMap.values().toArray(new TestImage[0]);

        currentLevelIndex = visionLevels.size() / 2;
    }

    public final TestUnit generateNext() {
        int level = currentLevelIndex;
        TestUnit unit = generateNextInternal();
        if (level == currentLevelIndex) {
            currentLevelCount++;
        } else {
            currentLevelCount = 0;
        }
        return unit;
    }

    protected abstract double getScale();

    protected abstract TestUnit generateNextInternal();

    public void higherLevelBinary() {

    }

    public void lowerLevelBinary() {

    }

    public TestImage[] getTestImageArray() {
        return testImages;
    }

    public Map<String, TestImage> getTestImageMap() {
        return testImageMap;
    }
}
