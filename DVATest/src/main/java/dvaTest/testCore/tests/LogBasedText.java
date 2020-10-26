package dvaTest.testCore.tests;

import common.Utility;
import dvaTest.gui.items.ScoreCounting;
import dvaTest.testCore.TestTypeException;
import dvaTest.testCore.testItems.TestImage;

import java.util.Map;

public abstract class LogBasedText extends Test {

    protected static final double[] VISION_LEVELS_5 =
            {4.0, 4.1, 4.2, 4.3, 4.4, 4.5, 4.6, 4.7, 4.8, 4.9, 5.0, 5.1, 5.2, 5.3};
    protected static final double[] VISION_LEVELS_DEC =
            {0.1, 0.12, 0.15, 0.2, 0.25, 0.3, 0.4, 0.5, 0.6, 0.8, 1.0, 1.2, 1.5, 2.0};
    protected static final double[] VISION_LEVELS_LOG_MAR =
            {1.0, 0.9, 0.8, 0.7, 0.6, 0.5, 0.4, 0.3, 0.2, 0.1, 0.0, -0.1, -0.2, -0.3};

    private static final String[] STR_LEVELS_5 = Utility.doubleArrayToStrArray(VISION_LEVELS_5);
    private static final String[] STR_LEVELS_DEC = Utility.doubleArrayToStrArray(VISION_LEVELS_DEC);
    private static final String[] STR_LEVELS_LOG_MAR = Utility.doubleArrayToStrArray(VISION_LEVELS_LOG_MAR);

//    protected final double[] visionLevels5;
//    protected final double[] visionLevelsFrac;
//    protected final double[] visionLevelsLogMar;

    public LogBasedText(Map<String, TestImage> testImageMap) {
        super(testImageMap);

//        this.visionLevels5 = visionLevels5;
//        this.visionLevelsFrac = visionLevelsFrac;
//        this.visionLevelsLogMar = visionLevelsLogMar;
    }

    @Override
    public int visionLevelCount() {
        return VISION_LEVELS_LOG_MAR.length;
//        return visionLevels5 != null ? visionLevels5.length :
//                (visionLevelsFrac != null ? visionLevelsFrac.length :
//                        visionLevelsLogMar.length);
    }

    @Override
    protected String[] getVisionLevels(ScoreCounting scoreCounting) {
        if (scoreCounting == ScoreCounting.FIVE) return STR_LEVELS_5;
        else if (scoreCounting == ScoreCounting.DEC) return STR_LEVELS_DEC;
        else if (scoreCounting == ScoreCounting.LOG_MAR) return STR_LEVELS_LOG_MAR;
        else throw new TestTypeException("No such score counting " + scoreCounting);
    }

    @Override
    protected double getScale(int levelIndex) {
        return Math.pow(10, VISION_LEVELS_LOG_MAR[levelIndex]);
    }

    @Override
    public int standardLevelIndex() {
        return 10;
    }
}
