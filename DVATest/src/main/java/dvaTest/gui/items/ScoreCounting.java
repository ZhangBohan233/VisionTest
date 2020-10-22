package dvaTest.gui.items;

import dvaTest.TestApp;

public enum ScoreCounting {
    FIVE(TestApp.getBundle().getString("fiveScore")),
    FRAC(TestApp.getBundle().getString("fracScore")),
    LOG_MAR(TestApp.getBundle().getString("logMarScore"));

    private final String showing;

    ScoreCounting(String showing) {
        this.showing = showing;
    }

    @Override
    public String toString() {
        return showing;
    }
}
