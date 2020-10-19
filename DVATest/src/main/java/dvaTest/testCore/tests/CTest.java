package dvaTest.testCore.tests;

import dvaTest.testCore.testItems.CTestImage;
import dvaTest.testCore.testItems.TestImage;

import java.util.List;
import java.util.Map;

public class CTest extends Test {

    public static Map<String, TestImage> ITEMS = Map.of(
            "right", new CTestImage("right", "c/C1.jpg"),
            "downRight", new CTestImage("downRight", "c/C2.jpg"),
            "down", new CTestImage("down", "c/C3.jpg"),
            "downLeft", new CTestImage("downLeft", "c/C4.jpg"),
            "left", new CTestImage("left", "c/C5.jpg"),
            "upLeft", new CTestImage("upLeft", "c/C6.jpg"),
            "up", new CTestImage("up", "c/C7.jpg"),
            "upRight", new CTestImage("upRight", "c/C8.jpg")
    );

    public static final CTest CTEST = new CTest(ITEMS);

    private CTest(Map<String, TestImage> testImages) {
        super(List.of(0.1, 0.12, 0.15, 0.2, 0.25, 0.3, 0.4, 0.5, 0.6, 0.8, 1.0, 1.2, 1.5), testImages);
    }

    @Override
    public int standardLevelIndex() {
        return 10;  // 标准为1.0
    }

    @Override
    public double standardHeightMm() {
        return 7.5;
    }

    @Override
    protected double getScale(int levelIndex) {
        return 1 / visionLevels.get(levelIndex);
    }

    @Override
    public TestUnit generate(int levelIndex) {
        int directionIndex = (int) (Math.random() * 8);
        return new TestUnit(visionLevels.get(levelIndex), getScale(levelIndex), testImages[directionIndex], this);
    }
}
