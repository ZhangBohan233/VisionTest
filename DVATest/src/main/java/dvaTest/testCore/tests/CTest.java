package dvaTest.testCore.tests;

import dvaTest.testCore.TestType;
import dvaTest.testCore.testItems.TestImage;

import java.util.Map;

public class CTest extends LogBasedText {

    public static final Map<String, TestImage> ITEMS = Map.of(
            "right", new TestImage("right", "c/right.jpg", TestType.LANDOLT),
            "downRight", new TestImage("downRight", "c/downRight.jpg", TestType.LANDOLT),
            "down", new TestImage("down", "c/down.jpg", TestType.LANDOLT),
            "downLeft", new TestImage("downLeft", "c/downLeft.jpg", TestType.LANDOLT),
            "left", new TestImage("left", "c/left.jpg", TestType.LANDOLT),
            "upLeft", new TestImage("upLeft", "c/upLeft.jpg", TestType.LANDOLT),
            "up", new TestImage("up", "c/up.jpg", TestType.LANDOLT),
            "upRight", new TestImage("upRight", "c/upRight.jpg", TestType.LANDOLT)
    );

    public static final CTest CTEST = new CTest(ITEMS);

    private CTest(Map<String, TestImage> testImages) {
        super(testImages);
    }

    @Override
    public double standardHeightMm(double distance) {
        return 7.272 / 5 * distance;
    }
}
