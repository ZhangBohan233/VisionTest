package dvaTest.testCore.tests;

import common.Signals;
import common.Utility;
import dvaTest.testCore.TestType;
import dvaTest.testCore.testItems.TestImage;

public class TestUnit {

    private final double visionLevel;
    private final double graphScale;

    private final Test test;
    private final TestImage testItem;

    TestUnit(double visionLevel, double graphScale, TestImage testItem, Test test) {
        this.visionLevel = visionLevel;
        this.graphScale = graphScale;
        this.testItem = testItem;
        this.test = test;
    }

    public TestImage getTestItem() {
        return testItem;
    }

    public double getGraphScale() {
        return graphScale;
    }

    public double getVisionLevel() {
        return visionLevel;
    }

    public byte[] toByteArray() {
        String name = testItem.getName();
        byte[] array = new byte[19 + name.length()];
        array[0] = Signals.NEXT_TEST_UNIT;
        array[1] = testItem.testType().toByte();
        Utility.doubleToBytes(visionLevel, array, 2);
        Utility.doubleToBytes(graphScale, array, 10);

        byte[] nameBytes = name.getBytes();
        array[18] = (byte) nameBytes.length;  // 没有检查 nameBytes.length() < 256
        System.arraycopy(nameBytes, 0, array, 19, nameBytes.length);

        return array;
    }

    public static TestUnit fromByteArray(byte[] array) {
        TestType testType = TestType.fromByte(array[1]);
        double visionLevel = Utility.bytesToDouble(array, 2);
        double graphScale = Utility.bytesToDouble(array, 10);
        int strLen = array[18] & 0xff;
        byte[] strBytes = new byte[strLen];
        System.arraycopy(array, 19, strBytes, 0, strLen);
        String name = new String(strBytes);
        TestImage testItem = TestImage.getByName(testType, name);
        return new TestUnit(visionLevel, graphScale, testItem, testType.getStaticTest());
    }

    public Test getTest() {
        return test;
    }
}
