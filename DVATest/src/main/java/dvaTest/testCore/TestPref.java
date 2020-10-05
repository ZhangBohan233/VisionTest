package dvaTest.testCore;

public class TestPref {

    private TestType testType;
    private long frameTimeMills;

    /**
     *
     */
    private int maxRepeat = 10;

    /**
     * 如连续答对此数量，直接跳级
     */
    private int directLevelUpCount = 3;

    public static class TestPrefBuilder {
        private final TestPref testPref = new TestPref();

        public TestPrefBuilder testType(TestType testType) {
            testPref.testType = testType;
            return this;
        }

        public TestPrefBuilder frameTimeMills(long frameTimeMills) {
            testPref.frameTimeMills = frameTimeMills;
            return this;
        }

        public TestPref build() {

            return testPref;
        }
    }

    public TestType getTestType() {
        return testType;
    }

    public long getFrameTimeMills() {
        return frameTimeMills;
    }
}
