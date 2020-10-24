package dvaTest.testCore.tests;

import common.Signals;
import common.Utility;
import dvaTest.testCore.TestType;
import dvaTest.testCore.testItems.TestImage;

public class TestUnit {

    private final double visionLevel;
    private final double graphScale;
    private final double distance;
    private final long timeInterval;

    private final Test test;
    private final TestImage testImage;

    TestUnit(double visionLevel, double graphScale, double distance, long timeInterval,
             TestImage testImage, Test test) {
        this.visionLevel = visionLevel;
        this.graphScale = graphScale;
        this.distance = distance;
        this.timeInterval = timeInterval;
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

    public long getTimeInterval() {
        return timeInterval;
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
        byte[] array = new byte[35 + name.length()];
        array[0] = Signals.NEXT_TEST_UNIT;
        array[1] = testImage.getTestType().toByte();
        Utility.doubleToBytes(visionLevel, array, 2);
        Utility.doubleToBytes(graphScale, array, 10);
        Utility.doubleToBytes(distance, array, 18);
        Utility.longToBytes(timeInterval, array, 26);

        byte[] nameBytes = name.getBytes();
        array[34] = (byte) nameBytes.length;  // 没有检查 nameBytes.length() < 256
        System.arraycopy(nameBytes, 0, array, 35, nameBytes.length);

        return array;
    }

    public static TestUnit fromByteArray(byte[] array) {
        TestType testType = TestType.fromByte(array[1]);
        double visionLevel = Utility.bytesToDouble(array, 2);
        double graphScale = Utility.bytesToDouble(array, 10);
        double distance = Utility.bytesToDouble(array, 18);
        long timeInterval = Utility.bytesToLong(array, 26);
        int strLen = array[34] & 0xff;
        byte[] strBytes = new byte[strLen];
        System.arraycopy(array, 35, strBytes, 0, strLen);
        String name = new String(strBytes);
        TestImage testItem = TestImage.getByName(testType, name);
        return new TestUnit(visionLevel, graphScale, distance, timeInterval, testItem, testType.getStaticTest());
    }

    public Test getTest() {
        return test;
    }
}
