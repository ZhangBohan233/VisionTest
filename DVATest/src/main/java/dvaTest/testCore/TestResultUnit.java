package dvaTest.testCore;

import dvaTest.testCore.tests.TestUnit;

public class TestResultUnit {
    private final TestUnit testUnit;
    private final String userInput;  // "" if no input
    private final boolean correct;

    TestResultUnit(TestUnit testUnit, String userInput) {
        this.testUnit = testUnit;
        this.userInput = userInput;
        this.correct = testUnit.getTestItem().getName().equals(userInput);
    }

    public String getUserInput() {
        return userInput;
    }

    public TestUnit getTestUnit() {
        return testUnit;
    }

    public boolean isCorrect() {
        return correct;
    }

    @Override
    public String toString() {
        return String.format("Given: %s, input: %s", testUnit, userInput);
    }
}
