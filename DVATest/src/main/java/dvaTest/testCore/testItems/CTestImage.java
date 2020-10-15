package dvaTest.testCore.testItems;

import dvaTest.testCore.TestType;

import java.util.List;

public class CTestImage extends TestImage {

    /**
     * 逆时针排列
     */
    public static final int BLANK = 0;
    public static final int UP_RIGHT = 1;
    public static final int UP = 2;
    public static final int UP_LEFT = 3;
    public static final int LEFT = 4;
    public static final int DOWN_LEFT = 5;
    public static final int DOWN = 6;
    public static final int DOWN_RIGHT = 7;
    public static final int RIGHT = 8;

    public static TestImage BLANK_ITEM = new CTestImage(BLANK, "blank", "c/C_BLANK.png");

    public static List<TestImage> ITEMS = List.of(
            new CTestImage(UP_RIGHT, "upRight", "c/C1.png"),
            new CTestImage(UP, "up", "c/C2.png"),
            new CTestImage(UP_LEFT, "upLeft", "c/C3.png"),
            new CTestImage(LEFT, "left", "c/C4.png"),
            new CTestImage(DOWN_LEFT, "downLeft", "c/C5.png"),
            new CTestImage(DOWN, "down", "c/C6.png"),
            new CTestImage(DOWN_RIGHT, "downRight", "c/C7.png"),
            new CTestImage(RIGHT, "right", "c/C8.png")
    );

    CTestImage(int ordNum, String description, String imagePath) {
        super(ordNum, description, imagePath);
    }

    @Override
    public TestType testType() {
        return TestType.C_CHART;
    }
}
