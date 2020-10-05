package dvaTest.testCore;

import common.Signals;
import dvaTest.testCore.testItems.CTestItem;
import dvaTest.testCore.testItems.TestItem;

import java.util.List;
import java.util.ResourceBundle;

public enum TestType {
    SNELLEN_CHART("snellenChart", Signals.SHOW_SNELLEN, null),
    C_CHART("cChart", Signals.SHOW_C, CTestItem.ITEMS),
    E_CHART("eChart", Signals.SHOW_E, null);

    private final String bundleKey;
    private final byte signal;
    private final List<TestItem> testItems;

    TestType(String bundleKey, byte signal, List<TestItem> testItems) {
        this.bundleKey = bundleKey;
        this.signal = signal;
        this.testItems = testItems;
    }

    public List<TestItem> getTestItems() {
        return testItems;
    }

    public byte toByte() {
        return (byte) ordinal();
    }

    public static TestType fromByte(byte b) {
        return values()[b & 0xff];
    }

    public byte getSignal() {
        return signal;
    }

    public String show(ResourceBundle bundle) {
        return bundle.getString(bundleKey);
    }
}
