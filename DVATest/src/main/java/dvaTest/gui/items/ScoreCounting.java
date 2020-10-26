package dvaTest.gui.items;

import dvaTest.TestApp;

public enum ScoreCounting {
    FIVE(TestApp.getBundle().getString("fiveScore")),
    DEC(TestApp.getBundle().getString("decScore")),
    LOG_MAR(TestApp.getBundle().getString("logMarScore")),
    FRAC_METER(TestApp.getBundle().getString("fracMeterScore"));

    private final String showing;

    ScoreCounting(String showing) {
        this.showing = showing;
    }

    @Override
    public String toString() {
        return showing;
    }
}
