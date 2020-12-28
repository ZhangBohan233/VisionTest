package dvaTest.testCore.tests;

import dvaTest.testCore.testItems.TestImage;

import java.util.Map;

public class StdLogTest extends LogBasedText {

    public static final Map<String, TestImage> ITEMS = Map.of(
            "left", new TestImage("left", "stdLog/left.jpg"),
            "up", new TestImage("up", "stdLog/up.jpg"),
            "right", new TestImage("right", "stdLog/right.jpg"),
            "down", new TestImage("down", "stdLog/down.jpg")
    );

    public StdLogTest() {
        super(ITEMS);
    }

    @Override
    public double standardHeightMm(double distance) {
        return 7.272 / 5 * distance;
    }
}
