package dvaTest.testCore.testItems;

import dvaTest.testCore.TestType;

import java.util.List;

public class CTestItem extends TestItem {

    public static List<TestItem> ITEMS = List.of(
            new CTestItem(1, "upRight", "c/C1.png"),
            new CTestItem(2, "up", "c/C2.png"),
            new CTestItem(3, "upLeft", "c/C3.png"),
            new CTestItem(4, "left", "c/C4.png"),
            new CTestItem(5, "downLeft", "c/C5.png"),
            new CTestItem(6, "down", "c/C6.png"),
            new CTestItem(7, "downRight", "c/C7.png"),
            new CTestItem(8, "right", "c/C8.png")
    );

    CTestItem(int ordNum, String description, String imagePath) {
        super(ordNum, description, imagePath);
    }

    @Override
    public TestType testType() {
        return TestType.C_CHART;
    }
}
