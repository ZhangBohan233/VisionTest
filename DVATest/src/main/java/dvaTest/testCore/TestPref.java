package dvaTest.testCore;

public class TestPref {

    private TestType testType;
    private long frameTimeMills;

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
