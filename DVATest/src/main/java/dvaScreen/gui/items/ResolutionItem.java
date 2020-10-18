package dvaScreen.gui.items;

import dvaScreen.Screen;

import java.util.List;

public class ResolutionItem {

    public static List<ResolutionItem> resolutionItems = List.of(
            new ResolutionItem(1024, 768),
            new ResolutionItem(1280, 720),
            new ResolutionItem(1280, 960),
            new ResolutionItem(1280, 1024),
            new ResolutionItem(1366, 768),
            new ResolutionItem(1680, 1050),
            new ResolutionItem(1920, 1080),
            new ResolutionItem(2560, 1440),
            new ResolutionItem(3840, 2160),
            new ResolutionItem(4096, 2160),
            new ResolutionItem(4096, 2304)
    );

    public static final int DEFAULT_INDEX = 6;

    private final int width;
    private final int height;
    private boolean isAutoDetected;

    public ResolutionItem(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void setAutoDetected(boolean autoDetected) {
        isAutoDetected = autoDetected;
    }

    @Override
    public String toString() {
        String base = width + "X" + height;

        return isAutoDetected ? base + Screen.getBundle().getString("autoDetected") : base;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
}
