package dvaTest.testCore.tests;

import java.util.List;

public abstract class Test {

    protected List<Double> visionLevels;
    protected int currentLevelIndex;

    /**
     * 在当前等级的测试次数
     */
    protected int currentLevelCount;

    public Test(List<Double> visionLevels) {
        this.visionLevels = visionLevels;
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
}
