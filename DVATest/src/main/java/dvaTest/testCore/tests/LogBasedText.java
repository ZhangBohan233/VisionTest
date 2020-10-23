package dvaTest.testCore.tests;

import dvaTest.gui.items.ScoreCounting;
import dvaTest.testCore.TestTypeException;
import dvaTest.testCore.testItems.TestImage;

import java.util.Map;

public abstract class LogBasedText extends Test {

    protected final double[] visionLevels5;
    protected final double[] visionLevelsFrac;
    protected final double[] visionLevelsLogMar;

    public LogBasedText(double[] visionLevels5,
                        double[] visionLevelsFrac,
                        double[] visionLevelsLogMar,
                        Map<String, TestImage> testImageMap) {
        super(testImageMap);

        this.visionLevels5 = visionLevels5;
        this.visionLevelsFrac = visionLevelsFrac;
        this.visionLevelsLogMar = visionLevelsLogMar;
    }

    public int visionLevelCount() {
        return visionLevels5 != null ? visionLevels5.length :
                (visionLevelsFrac != null ? visionLevelsFrac.length :
                        visionLevelsLogMar.length);
    }

    protected double[] getVisionLevels(ScoreCounting scoreCounting) {
        if (scoreCounting == ScoreCounting.FIVE) return visionLevels5;
        else if (scoreCounting == ScoreCounting.FRAC) return visionLevelsFrac;
        else if (scoreCounting == ScoreCounting.LOG_MAR) return visionLevelsLogMar;
        else throw new TestTypeException("No such score counting. ");
    }
}
