package dvaTest.testCore;

import dvaTest.gui.items.ScoreCounting;

public class TestPref {

    private TestType testType;
    private ScoreCounting scoreCounting;
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

        public TestPrefBuilder scoreCounting(ScoreCounting scoreCounting) {
            testPref.scoreCounting = scoreCounting;
            return this;
        }

        public TestPref build() {
            return testPref;
        }
    }

    public TestType getTestType() {
        return testType;
    }

    public ScoreCounting getScoreCounting() {
        return scoreCounting;
    }

    public long getFrameTimeMills() {
        return frameTimeMills;
    }
}
