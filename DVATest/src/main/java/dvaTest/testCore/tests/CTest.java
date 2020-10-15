package dvaTest.testCore.tests;

import dvaTest.testCore.testItems.CTestImage;
import dvaTest.testCore.testItems.TestImage;

import java.util.List;
import java.util.Map;

public class CTest extends Test {

    public static Map<String, TestImage> ITEMS = Map.of(
            "upRight", new CTestImage("upRight", "c/C1.png"),
            "up", new CTestImage("up", "c/C2.png"),
            "upLeft", new CTestImage("upLeft", "c/C3.png"),
            "left", new CTestImage("left", "c/C4.png"),
            "downLeft", new CTestImage("downLeft", "c/C5.png"),
            "down", new CTestImage("down", "c/C6.png"),
            "downRight", new CTestImage("downRight", "c/C7.png"),
            "right", new CTestImage("right", "c/C8.png")
    );

    public CTest() {
        super(
                List.of(0.1, 0.12, 0.15, 0.2, 0.25, 0.3, 0.4, 0.5, 0.6, 0.8, 1.0, 1.2, 1.5),
                ITEMS
        );
    }

    @Override
    protected double getScale() {
        return 1 / visionLevels.get(currentLevelIndex);
    }

    @Override
    public TestUnit generateNextInternal() {
        int directionIndex = (int) (Math.random() * 8);
        return new TestUnit(visionLevels.get(currentLevelIndex), getScale(), testImages[directionIndex]);
    }
}
