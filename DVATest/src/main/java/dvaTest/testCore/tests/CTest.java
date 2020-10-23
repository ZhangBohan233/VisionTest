package dvaTest.testCore.tests;

import dvaTest.testCore.TestType;
import dvaTest.testCore.testItems.TestImage;

import java.util.Map;

public class CTest extends LogBasedText {

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

    private static final double[] VISION_LEVELS_5 =
            {4.0, 4.1, 4.2, 4.3, 4.4, 4.5, 4.6, 4.7, 4.8, 4.9, 5.0, 5.1, 5.2, 5.3};
    private static final double[] VISION_LEVELS_FRAC =
            {0.1, 0.12, 0.15, 0.2, 0.25, 0.3, 0.4, 0.5, 0.6, 0.8, 1.0, 1.2, 1.5, 2.0};
    private static final double[] VISION_LEVELS_LOG_MAR =
            {1.0, 0.9, 0.8, 0.7, 0.6, 0.5, 0.4, 0.3, 0.2, 0.1, 0.0, -0.1, -0.2, -0.3};

    public static final CTest CTEST = new CTest(ITEMS);

    private CTest(Map<String, TestImage> testImages) {
        super(VISION_LEVELS_5,
                VISION_LEVELS_FRAC,
                VISION_LEVELS_LOG_MAR,
                testImages);
    }

    @Override
    public int standardLevelIndex() {
        return 10;  // 标准为1.0
    }

    @Override
    public double standardHeightMm(double distance) {
        return 7.5 / 5 * distance;
    }

    @Override
    protected double getScale(int levelIndex) {
        return 1 / visionLevelsFrac[levelIndex];
    }
}
