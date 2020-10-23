package dvaTest.testCore.tests;

import common.Signals;
import common.Utility;
import dvaTest.testCore.TestType;
import dvaTest.testCore.testItems.TestImage;

public class TestUnit {

    private final double visionLevel;
    private final double graphScale;
    private final double distance;

    private final Test test;
    private final TestImage testImage;

    TestUnit(double visionLevel, double graphScale, double distance, TestImage testImage, Test test) {
        this.visionLevel = visionLevel;
        this.graphScale = graphScale;
        this.distance = distance;
        this.testImage = testImage;
        this.test = test;
    }

    public TestImage getTestImage() {
        return testImage;
    }

    public double getGraphScale() {
        return graphScale;
    }

    public double getVisionLevel() {
        return visionLevel;
    }

    public double getDistance() {
        return distance;
    }

    @Override
    public String toString() {
        return "TestUnit{" +
                "visionLevel=" + visionLevel +
                ", testItem=" + testImage +
                '}';
    }

    public byte[] toByteArray() {
        String name = testImage.getName();
        byte[] array = new byte[27 + name.length()];
        array[0] = Signals.NEXT_TEST_UNIT;
        array[1] = testImage.getTestType().toByte();
        Utility.doubleToBytes(visionLevel, array, 2);
        Utility.doubleToBytes(graphScale, array, 10);
        Utility.doubleToBytes(distance, array, 18);

        byte[] nameBytes = name.getBytes();
        array[26] = (byte) nameBytes.length;  // 没有检查 nameBytes.length() < 256
        System.arraycopy(nameBytes, 0, array, 27, nameBytes.length);

        return array;
    }

    public static TestUnit fromByteArray(byte[] array) {
        TestType testType = TestType.fromByte(array[1]);
        double visionLevel = Utility.bytesToDouble(array, 2);
        double graphScale = Utility.bytesToDouble(array, 10);
        double distance = Utility.bytesToDouble(array, 18);
        int strLen = array[26] & 0xff;
        byte[] strBytes = new byte[strLen];
        System.arraycopy(array, 27, strBytes, 0, strLen);
        String name = new String(strBytes);
        TestImage testItem = TestImage.getByName(testType, name);
        return new TestUnit(visionLevel, graphScale, distance, testItem, testType.getStaticTest());
    }

    public Test getTest() {
        return test;
    }
}
