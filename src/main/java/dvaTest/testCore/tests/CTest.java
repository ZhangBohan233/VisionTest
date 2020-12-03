package dvaTest.testCore.tests;

import dvaTest.testCore.TestType;
import dvaTest.testCore.testItems.TestImage;

import java.util.Map;

public class CTest extends LogBasedText {

    public static final Map<String, TestImage> ITEMS = Map.of(
            "right", new TestImage("right", "c/right.jpg"),
            "downRight", new TestImage("downRight", "c/downRight.jpg"),
            "down", new TestImage("down", "c/down.jpg"),
            "downLeft", new TestImage("downLeft", "c/downLeft.jpg"),
            "left", new TestImage("left", "c/left.jpg"),
            "upLeft", new TestImage("upLeft", "c/upLeft.jpg"),
            "up", new TestImage("up", "c/up.jpg"),
            "upRight", new TestImage("upRight", "c/upRight.jpg")
    );

    public CTest() {
        super(ITEMS);
    }

    @Override
    public double standardHeightMm(double distance) {
        return 7.272 / 5 * distance;
    }
}
