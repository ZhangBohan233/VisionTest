package dvaTest.testCore.tests;

import dvaTest.testCore.testItems.CTestImage;

import java.util.List;

public class CTest extends Test {

    public CTest() {
        super(List.of(0.1, 0.12, 0.15, 0.2, 0.25, 0.3, 0.4, 0.5, 0.6, 0.8, 1.0, 1.2, 1.5));
    }

    @Override
    protected double getScale() {
        return 1 / visionLevels.get(currentLevelIndex);
    }

    @Override
    public TestUnit generateNextInternal() {
        int directionIndex = (int) (Math.random() * 8);
        return new TestUnit(visionLevels.get(currentLevelIndex), getScale(), CTestImage.ITEMS.get(directionIndex));
    }
}
