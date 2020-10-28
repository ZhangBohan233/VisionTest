package dvaTest.gui.items;

import dvaTest.TestApp;

public enum ScoreCounting {
    FIVE(TestApp.getBundle().getString("fiveScore"), true),
    DEC(TestApp.getBundle().getString("decScore"), true),
    LOG_MAR(TestApp.getBundle().getString("logMarScore"), true),
    FRAC_METER(TestApp.getBundle().getString("fracMeterScore"), false);

    private final String showing;
    public final boolean isLogMar;

    ScoreCounting(String showing, boolean isLogMar) {
        this.showing = showing;
        this.isLogMar = isLogMar;
    }

    @Override
    public String toString() {
        return showing;
    }
}
