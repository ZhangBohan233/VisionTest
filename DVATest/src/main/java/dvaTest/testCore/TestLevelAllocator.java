package dvaTest.testCore;

public class TestLevelAllocator {
    private final int totalLevelCount;
    private int currentLevelIndex;

    /**
     * 在用户第一次错误之前，该值为{@code true}
     */
    private boolean neverFalse = true;

    public TestLevelAllocator(int totalLevelCount) {
        this.totalLevelCount = totalLevelCount;

        currentLevelIndex = totalLevelCount / 2;
    }

    public boolean hasNext() {
        return true;
    }

    public int nextLevel() {
        // todo

        return currentLevelIndex;
    }

    public void correctResult() {

    }

    public void incorrectResult() {

        neverFalse = false;
    }
}
