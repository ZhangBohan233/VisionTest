package dvaTest.testCore;

import common.Signals;
import dvaTest.testCore.testItems.CTestImage;
import dvaTest.testCore.testItems.TestImage;
import dvaTest.testCore.tests.CTest;
import dvaTest.testCore.tests.Test;

import java.util.Collection;
import java.util.Map;
import java.util.ResourceBundle;

public enum TestType {
    SNELLEN_CHART("snellenChart", Signals.SHOW_SNELLEN, null),
    C_CHART("cChart", Signals.SHOW_C, CTest.ITEMS),
    E_CHART("eChart", Signals.SHOW_E, null);

    private final String bundleKey;
    private final byte signal;
    private final Map<String, TestImage> testItems;

    TestType(String bundleKey, byte signal, Map<String, TestImage> testItems) {
        this.bundleKey = bundleKey;
        this.signal = signal;
        this.testItems = testItems;
    }

    public static TestType fromByte(byte b) {
        return values()[b & 0xff];
    }

    public Map<String, TestImage> getTestItems() {
        return testItems;
    }

    public byte toByte() {
        return (byte) ordinal();
    }

    public byte getSignal() {
        return signal;
    }

    public String show(ResourceBundle bundle) {
        return bundle.getString(bundleKey);
    }

    public Test getStaticTest() {
        Test test;
        if (this == SNELLEN_CHART) {
            test = null;
        } else if (this == C_CHART) {
            test = CTest.CTEST;
        } else if (this == E_CHART) {
            test = null;
        } else {
            throw new TestTypeException("Unexpected test type. ");
        }
        return test;
    }
}
