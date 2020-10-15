package dvaTest.testCore.testItems;

import dvaTest.testCore.TestType;

import java.util.List;
import java.util.Map;

public class CTestImage extends TestImage {

//    /**
//     * 逆时针排列
//     */
//    public static final int BLANK = 0;
//    public static final int UP_RIGHT = 1;
//    public static final int UP = 2;
//    public static final int UP_LEFT = 3;
//    public static final int LEFT = 4;
//    public static final int DOWN_LEFT = 5;
//    public static final int DOWN = 6;
//    public static final int DOWN_RIGHT = 7;
//    public static final int RIGHT = 8;

//    public static TestImage BLANK_ITEM = new CTestImage("blank", "c/C_BLANK.png");
//
//    public static Map<String, TestImage> ITEMS = Map.of(
//            "upRight", new CTestImage("upRight", "c/C1.png"),
//            "up", new CTestImage("up", "c/C2.png"),
//            "upLeft", new CTestImage("upLeft", "c/C3.png"),
//            "left", new CTestImage("left", "c/C4.png"),
//            "downLeft", new CTestImage("downLeft", "c/C5.png"),
//            "down", new CTestImage("down", "c/C6.png"),
//            "downRight", new CTestImage("downRight", "c/C7.png"),
//            "right", new CTestImage("right", "c/C8.png")
//    );

    public CTestImage(String name, String imagePath) {
        super(name, imagePath);
    }

    @Override
    public TestType testType() {
        return TestType.C_CHART;
    }
}
