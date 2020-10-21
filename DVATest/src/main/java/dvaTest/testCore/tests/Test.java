package dvaTest.testCore.tests;

import dvaTest.testCore.testItems.TestImage;

import java.util.List;
import java.util.Map;

/**
 * Static test
 */
public abstract class Test {

    protected final double[] visionLevels;
    protected final Map<String, TestImage> testImageMap;
    protected final TestImage[] testImages;

    public Test(double[] visionLevels, Map<String, TestImage> testImageMap) {
        this.visionLevels = visionLevels;
        this.testImageMap = testImageMap;

        testImages = testImageMap.values().toArray(new TestImage[0]);
    }

    public int visionLevelCount() {
        return visionLevels.length;
    }

    public TestUnit generate(int levelIndex) {
        int directionIndex = (int) (Math.random() * testImages.length);
        return new TestUnit(visionLevels[levelIndex], getScale(levelIndex), testImages[directionIndex], this);
    }

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
     * @return the absolute height of image of the standard level, in millimeter(mm).
     */
    public abstract double standardHeightMm();

    public TestImage[] getTestImageArray() {
        return testImages;
    }

    public Map<String, TestImage> getTestImageMap() {
        return testImageMap;
    }
}
