package dvaTest.testCore.tests;

import dvaTest.testCore.TestType;
import dvaTest.testCore.testItems.TestImage;

import java.util.List;
import java.util.Map;

public class StdLogTest extends Test {

    public static final Map<String, TestImage> ITEMS = Map.of(
            "left", new TestImage("left", "stdLog/LOG1.jpg", TestType.STD_LOG_CHART),
            "up", new TestImage("up", "stdLog/LOG2.jpg", TestType.STD_LOG_CHART),
            "right", new TestImage("right", "stdLog/LOG3.jpg", TestType.STD_LOG_CHART),
            "down", new TestImage("down", "stdLog/LOG4.jpg", TestType.STD_LOG_CHART)
    );

    private static final double[] VISION_LEVELS =
            {4.0, 4.1, 4.2, 4.3, 4.4, 4.5, 4.6, 4.7, 4.8, 4.9, 5.0, 5.1, 5.2, 5.3};

    /**
     * Calculated in common.Utility
     */
    private static final double[] VISION_SCALES =
            {10.0, 7.9433, 6.3096, 5.0119, 3.9811, 3.1623, 2.5119, 1.9953, 1.5849, 1.2589, 1.0, 0.7943, 0.6310, 0.5012};

    public static final StdLogTest STD_LOG_TEST = new StdLogTest(ITEMS);

    private StdLogTest(Map<String, TestImage> testImages) {
        super(VISION_LEVELS, testImages);
    }

    @Override
    protected double getScale(int levelIndex) {
        return VISION_SCALES[levelIndex];
    }

    @Override
    public int standardLevelIndex() {
        return 10;
    }

    @Override
    public double standardHeightMm() {
        return 7.272;
    }
}
