package dvaTest.testCore.tests;

import dvaTest.gui.items.ScoreCounting;
import dvaTest.testCore.TestPref;
import dvaTest.testCore.testItems.TestImage;

import java.util.Map;

/**
 * Static test
 */
public abstract class Test {

    protected final Map<String, TestImage> testImageMap;
    protected final TestImage[] testImages;

    public Test(Map<String, TestImage> testImageMap) {
        this.testImageMap = testImageMap;

        testImages = testImageMap.values().toArray(new TestImage[0]);
    }

    public abstract int visionLevelCount();

    public abstract String getLevelString(ScoreCounting scoreCounting, int levelIndex);

    public TestUnit generate(int levelIndex, TestPref testPref) {
        int directionIndex = (int) (Math.random() * testImages.length);
        return new TestUnit(
                getVisionLevels(testPref.getScoreCounting())[levelIndex],
                getScale(levelIndex),
                testPref.getDistance(),
                testPref.getIntervalMills(),
                testImages[directionIndex],
                testPref.getTestType());
    }

    protected abstract String[] getVisionLevels(ScoreCounting scoreCounting);

    /**
     * 当前视标边长相对于标准视标边长的倍数。
     * <p>
     * 如小数视力记录中，若视力为 0.1 则返回 10.0
     *
     * @param levelIndex current level index
     * @return scale factor of the image at {@code levelIndex}, relative to image at standard level
     */
    protected abstract double getScale(int levelIndex);

    /**
     * 标准等级在所有等级中的位置。等级均按照视标尺寸从大到小排列。
     *
     * @return the index of standard level in all levels.
     */
    public abstract int standardLevelIndex();

    /**
     * 标准视力等级视标的高度（毫米）
     *
     * @param distance view distance
     * @return the absolute height of image of the standard level, in millimeter(mm).
     */
    public abstract double standardHeightMm(double distance);

    public TestImage[] getTestImageArray() {
        return testImages;
    }

    public Map<String, TestImage> getTestImageMap() {
        return testImageMap;
    }
}
