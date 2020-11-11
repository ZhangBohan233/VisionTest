package dvaTest.testCore.tests;

import dvaTest.testCore.TestType;
import dvaTest.testCore.testItems.TestImage;

import java.util.Map;

public class StdLogTest extends LogBasedText {

    public static final Map<String, TestImage> ITEMS = Map.of(
            "left", new TestImage("left", "stdLog/left.jpg", TestType.STD_LOG),
            "up", new TestImage("up", "stdLog/up.jpg", TestType.STD_LOG),
            "right", new TestImage("right", "stdLog/right.jpg", TestType.STD_LOG),
            "down", new TestImage("down", "stdLog/down.jpg", TestType.STD_LOG)
    );

//    /**
//     * Calculated in common.Utility
//     */
//    private static final double[] VISION_SCALES =
//            {10.0, 7.9433, 6.3096, 5.0119, 3.9811, 3.1623, 2.5119, 1.9953, 1.5849, 1.2589, 1.0, 0.7943, 0.6310, 0.5012};

    public static final StdLogTest STD_LOG_TEST = new StdLogTest(ITEMS);

    private StdLogTest(Map<String, TestImage> testImages) {
        super(testImages);
    }

    @Override
    public double standardHeightMm(double distance) {
        return 7.272 / 5 * distance;
    }
}
