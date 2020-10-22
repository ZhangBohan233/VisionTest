package dvaTest.testCore.tests;

import dvaTest.gui.items.ScoreCounting;
import dvaTest.testCore.TestTypeException;
import dvaTest.testCore.testItems.TestImage;

import java.util.Map;

/**
 * Static test
 */
public abstract class Test {

    protected final double[] visionLevels5;
    protected final double[] visionLevelsFrac;
    protected final double[] visionLevelsLogMar;
    protected final Map<String, TestImage> testImageMap;
    protected final TestImage[] testImages;

    public Test(double[] visionLevels5, double[] visionLevelsFrac, double[] visionLevelsLogMar,
                Map<String, TestImage> testImageMap) {
        this.visionLevels5 = visionLevels5;
        this.visionLevelsFrac = visionLevelsFrac;
        this.visionLevelsLogMar = visionLevelsLogMar;
        this.testImageMap = testImageMap;

        testImages = testImageMap.values().toArray(new TestImage[0]);
    }

    public int visionLevelCount() {
        return visionLevels5 != null ? visionLevels5.length :
                (visionLevelsFrac != null ? visionLevelsFrac.length :
                        visionLevelsLogMar.length);
    }

    public TestUnit generate(int levelIndex, ScoreCounting scoreCounting) {
        int directionIndex = (int) (Math.random() * testImages.length);
        return new TestUnit(
                getVisionLevels(scoreCounting)[levelIndex],
                getScale(levelIndex),
                testImages[directionIndex],
                this);
    }

    private double[] getVisionLevels(ScoreCounting scoreCounting) {
        if (scoreCounting == ScoreCounting.FIVE) return visionLevels5;
        else if (scoreCounting == ScoreCounting.FRAC) return visionLevelsFrac;
        else if (scoreCounting == ScoreCounting.LOG_MAR) return visionLevelsLogMar;
        else throw new TestTypeException("No such score counting. ");
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
