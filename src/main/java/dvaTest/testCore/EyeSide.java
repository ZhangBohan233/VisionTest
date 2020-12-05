package dvaTest.testCore;

import dvaTest.TestApp;

public enum EyeSide {
    LEFT(TestApp.getBundle().getString("leftEye")),
    RIGHT(TestApp.getBundle().getString("rightEye")),
    BOTH(TestApp.getBundle().getString("bothEyes"));

    private final String showingName;

    EyeSide(String showingName) {
        this.showingName = showingName;
    }

    @Override
    public String toString() {
        return showingName;
    }
}
