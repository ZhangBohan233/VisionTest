package dvaScreen.gui.items;

import dvaScreen.Screen;

import java.util.List;

public class WindowsScaleItem {

    public static final List<WindowsScaleItem> SCALE_ITEMS = List.of(
            new WindowsScaleItem(1.0),
            new WindowsScaleItem(1.25),
            new WindowsScaleItem(1.5),
            new WindowsScaleItem(1.75)
    );

    private final double scale;
    private boolean autoDetected;

    public WindowsScaleItem(double scale) {
        this.scale = scale;
    }

    public void setAutoDetected(boolean autoDetected) {
        this.autoDetected = autoDetected;
    }

    public double getScale() {
        return scale;
    }

    @Override
    public String toString() {
        String base = (int) (scale * 100) + "%";

        return autoDetected ? base + Screen.getBundle().getString("autoDetected") : base;
    }
}
