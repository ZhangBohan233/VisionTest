package dvaTest.testCore.tests;

import dvaTest.testCore.TestType;
import dvaTest.testCore.testItems.TestImage;

import java.util.Map;

public class CTest extends LogBasedText {

    public static final Map<String, TestImage> ITEMS = Map.of(
            "right", new TestImage("right", "c/right.jpg", TestType.C_CHART),
            "downRight", new TestImage("downRight", "c/downRight.jpg", TestType.C_CHART),
            "down", new TestImage("down", "c/down.jpg", TestType.C_CHART),
            "downLeft", new TestImage("downLeft", "c/downLeft.jpg", TestType.C_CHART),
            "left", new TestImage("left", "c/left.jpg", TestType.C_CHART),
            "upLeft", new TestImage("upLeft", "c/upLeft.jpg", TestType.C_CHART),
            "up", new TestImage("up", "c/up.jpg", TestType.C_CHART),
            "upRight", new TestImage("upRight", "c/upRight.jpg", TestType.C_CHART)
    );

    public static final CTest CTEST = new CTest(ITEMS);

    private CTest(Map<String, TestImage> testImages) {
        super(testImages);
    }

    @Override
    public double standardHeightMm(double distance) {
        return 7.5 / 5 * distance;
    }
}
