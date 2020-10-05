package dvaTest.testCore.tests;

import common.Signals;
import common.Utility;
import dvaTest.testCore.TestType;
import dvaTest.testCore.testItems.TestItem;

public class TestUnit {

    private final double visionLevel;
    private final double graphScale;

    private final TestItem testItem;

    TestUnit(double visionLevel, double graphScale, TestItem testItem) {
        this.visionLevel = visionLevel;
        this.graphScale = graphScale;
        this.testItem = testItem;
    }

    public TestItem getTestItem() {
        return testItem;
    }

    public double getGraphScale() {
        return graphScale;
    }

    public double getVisionLevel() {
        return visionLevel;
    }

    public byte[] toByteArray() {
        byte[] array = new byte[19];
        array[0] = Signals.NEXT_TEST_UNIT;
        array[1] = testItem.getOrdNum();
        array[2] = testItem.testType().toByte();
        Utility.doubleToBytes(visionLevel, array, 3);
        Utility.doubleToBytes(graphScale, array, 11);

        return array;
    }

    public static TestUnit fromByteArray(byte[] array) {
        byte ordNum = array[1];
        TestType testType = TestType.fromByte(array[2]);
        double visionLevel = Utility.bytesToDouble(array, 3);
        double graphScale = Utility.bytesToDouble(array, 11);
        TestItem testItem = TestItem.getByOrdNum(testType, ordNum);
        return new TestUnit(visionLevel, graphScale, testItem);
    }
}
