package dvaTest.testCore;

public class TestLevelAllocator {
    private final int totalLevelCount;
    private int curLvIndex;
    private int curLvContCorrCount;  // current level continuous correct count
    private int curLvContFailCount;

    /**
     * 每个等级进行的测试次数
     */
    private int[] levelTestsCount;

    /**
     * 在用户第一次错误之前，该值为{@code true}
     */
    private boolean neverFail = true;
    private boolean neverCorr = true;

    public TestLevelAllocator(int totalLevelCount, int initialLevelIndex) {
        this.totalLevelCount = totalLevelCount;
        this.levelTestsCount = new int[totalLevelCount];

        curLvIndex = initialLevelIndex;
    }

    public boolean hasNext() {
        return levelTestsCount[curLvIndex] < 5;
    }

    public int next() {
        levelTestsCount[curLvIndex]++;
        return curLvIndex;
    }

    public void correctResult() {
        neverCorr = false;
        curLvContFailCount = 0;
        if (neverFail) {
            // binary
            curLvIndex = (totalLevelCount - curLvIndex) / 2 + curLvIndex;
            curLvContCorrCount = 0;
            return;
        }
        curLvContCorrCount++;
        if (curLvContCorrCount >= 3) {
            curLvIndex++;
            curLvContCorrCount = 0;
        }
    }

    public void incorrectResult() {
        neverFail = false;
        curLvContCorrCount = 0;
        if (neverCorr) {
            // binary
            curLvIndex /= 2;
            curLvContFailCount = 0;
            return;
        }

        curLvContFailCount++;
        if (curLvContFailCount >= 3) {
            curLvIndex--;
            curLvContFailCount = 0;
        }
    }
}
