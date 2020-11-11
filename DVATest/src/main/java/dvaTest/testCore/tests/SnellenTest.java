package dvaTest.testCore.tests;

import dvaTest.gui.items.ScoreCounting;
import dvaTest.testCore.TestType;
import dvaTest.testCore.TestTypeException;
import dvaTest.testCore.testItems.TestImage;

import java.util.Map;

public class SnellenTest extends Test {

    public static final Map<String, TestImage> ITEMS = Map.of(
            "C", new TestImage("C", "snellen/C2.jpg", TestType.SNELLEN),
            "D", new TestImage("D", "snellen/D2.jpg", TestType.SNELLEN),
            "E", new TestImage("E", "snellen/E.jpg", TestType.SNELLEN),
            "F", new TestImage("F", "snellen/F.jpg", TestType.SNELLEN),
            "L", new TestImage("L", "snellen/L.jpg", TestType.SNELLEN),
            "O", new TestImage("O", "snellen/O.jpg", TestType.SNELLEN),
            "P", new TestImage("P", "snellen/P2.jpg", TestType.SNELLEN),
            "T", new TestImage("T", "snellen/T.jpg", TestType.SNELLEN),
            "Z", new TestImage("Z", "snellen/Z.jpg", TestType.SNELLEN)
    );

    private static final double[] SCALES =
            {10.0, 6.0, 4.0, 3.0, 2.0, 1.5, 1.0, 0.8333, 0.6667};

    private static final String[] STR_LEVEL_METERS =
            {"6/60", "6/36", "6/24", "6/18", "6/12", "6/9", "6/6", "6/5", "6/4"};

    public static final SnellenTest SNELLEN_TEST = new SnellenTest(ITEMS);

    private SnellenTest(Map<String, TestImage> testImageMap) {
        super(testImageMap);
    }

    @Override
    public int visionLevelCount() {
        return STR_LEVEL_METERS.length;
    }

    @Override
    protected String[] getVisionLevels(ScoreCounting scoreCounting) {
        if (scoreCounting == ScoreCounting.FRAC_METER) return STR_LEVEL_METERS;
        else throw new TestTypeException("No such score counting " + scoreCounting);
    }

    @Override
    protected double getScale(int levelIndex) {
        return SCALES[levelIndex];
    }

    @Override
    public int standardLevelIndex() {
        return 6;
    }

    @Override
    public double standardHeightMm(double distance) {
        return 8.74 / 5 * distance;
    }
}
