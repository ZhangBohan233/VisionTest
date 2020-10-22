package dvaTest.testCore.tests;

import dvaTest.testCore.TestType;
import dvaTest.testCore.testItems.TestImage;

import java.util.Map;

public class CTest extends Test {

    public static final Map<String, TestImage> ITEMS = Map.of(
            "right", new TestImage("right", "c/C1.jpg", TestType.C_CHART),
            "downRight", new TestImage("downRight", "c/C2.jpg", TestType.C_CHART),
            "down", new TestImage("down", "c/C3.jpg", TestType.C_CHART),
            "downLeft", new TestImage("downLeft", "c/C4.jpg", TestType.C_CHART),
            "left", new TestImage("left", "c/C5.jpg", TestType.C_CHART),
            "upLeft", new TestImage("upLeft", "c/C6.jpg", TestType.C_CHART),
            "up", new TestImage("up", "c/C7.jpg", TestType.C_CHART),
            "upRight", new TestImage("upRight", "c/C8.jpg", TestType.C_CHART)
    );

    public static final CTest CTEST = new CTest(ITEMS);

    private CTest(Map<String, TestImage> testImages) {
        super(null,
                new double[]{0.1, 0.12, 0.15, 0.2, 0.25, 0.3, 0.4, 0.5, 0.6, 0.8, 1.0, 1.2, 1.5},
                null,
                testImages);
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
        return 1 / visionLevelsFrac[levelIndex];
    }
}
