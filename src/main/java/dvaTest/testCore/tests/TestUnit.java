package dvaTest.testCore.tests;

import common.Signals;
import common.Utility;
import dvaTest.testCore.TestType;
import dvaTest.testCore.testItems.TestImage;

public class TestUnit {

    private final String visionLevel;
    private final double graphScale;
    private final double distance;
    private final long timeInterval;

    private final TestType testType;
    private final TestImage testImage;

    TestUnit(String visionLevel, double graphScale, double distance, long timeInterval,
             TestImage testImage, TestType testType) {
        this.visionLevel = visionLevel;
        this.graphScale = graphScale;
        this.distance = distance;
        this.timeInterval = timeInterval;
        this.testImage = testImage;
        this.testType = testType;
    }

    /**
     * 从字节数据中重建一个{@code TestUnit}实例
     *
     * @param array 从socket接收的字节数据
     * @return 根据字节数据重建的{@code TestUnit}实例
     */
    public static TestUnit fromByteArray(byte[] array) {
        TestType testType = TestType.fromByte(array[1]);
        double graphScale = Utility.bytesToDouble(array, 2);
        double distance = Utility.bytesToDouble(array, 10);
        long timeInterval = Utility.bytesToLong(array, 18);
        int strLen = array[26] & 0xff;
        byte[] strBytes = new byte[strLen];
        System.arraycopy(array, 27, strBytes, 0, strLen);
        String name = new String(strBytes);
        TestImage testImage = testType.getTest().getTestImageMap().get(name);
        int levelLen = array[27 + strLen] & 0xff;
        byte[] levelBytes = new byte[levelLen];
        System.arraycopy(array, 28 + strLen, levelBytes, 0, levelLen);
        String visionLevel = new String(levelBytes);
        return new TestUnit(visionLevel, graphScale, distance, timeInterval, testImage, testType);
    }

    public TestImage getTestImage() {
        return testImage;
    }

    public double getGraphScale() {
        return graphScale;
    }

    public String getVisionLevel() {
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

    /**
     * 导出该实例为一个字节数组
     * <p>
     * 数组结构：
     * 0: signal
     * 1: testType
     * 2~9: graphScale
     * 10~17: distance
     * 18~25: time interval
     * 26: length of name (nameLen)
     * 27~27+nameLen-1: name
     * 27+nameLen: length of level (levelLen)
     * 28+nameLen~28+nameLen+levelLen-1: level
     *
     * @return byte representation of this TestUnit
     */
    public byte[] toByteArray() {
        String name = testImage.getName();
        byte[] nameBytes = name.getBytes();
        byte[] levelBytes = visionLevel.getBytes();

        byte[] array = new byte[28 + nameBytes.length + levelBytes.length];
        array[0] = Signals.NEXT_TEST_UNIT;
        array[1] = testType.toByte();
        Utility.doubleToBytes(graphScale, array, 2);
        Utility.doubleToBytes(distance, array, 10);
        Utility.longToBytes(timeInterval, array, 18);

        array[26] = (byte) nameBytes.length;  // 没有检查 nameBytes.length() < 256
        System.arraycopy(nameBytes, 0, array, 27, nameBytes.length);

        array[27 + nameBytes.length] = (byte) levelBytes.length;
        System.arraycopy(levelBytes,
                0,
                array,
                28 + nameBytes.length,
                levelBytes.length);

        return array;
    }

    public TestType getTestType() {
        return testType;
    }
}
