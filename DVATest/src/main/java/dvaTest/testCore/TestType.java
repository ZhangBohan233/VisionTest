package dvaTest.testCore;

import java.util.ResourceBundle;

public enum TestType {

    SNELLEN_CHART("snellenChart"), C_CHART("cChart"), E_CHART("eChart");

    private final String bundleKey;

    TestType(String bundleKey) {
        this.bundleKey = bundleKey;
    }

    public byte toByte() {
        return (byte) ordinal();
    }

    public String show(ResourceBundle bundle) {
        return bundle.getString(bundleKey);
    }
}
