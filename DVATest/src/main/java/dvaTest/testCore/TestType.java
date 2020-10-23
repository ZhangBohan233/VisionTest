package dvaTest.testCore;

import common.Signals;
import dvaTest.testCore.testItems.TestImage;
import dvaTest.testCore.tests.CTest;
import dvaTest.testCore.tests.StdLogTest;
import dvaTest.testCore.tests.Test;

import java.util.Collection;
import java.util.Map;
import java.util.ResourceBundle;

public enum TestType {
    SNELLEN_CHART("snellenChart", Signals.SHOW_SNELLEN),
    C_CHART("cChart", Signals.SHOW_C),
    E_CHART("eChart", Signals.SHOW_E),
    STD_LOG_CHART("stdLogChart", Signals.SHOW_STD_LOG);

    private final String bundleKey;
    private final byte signal;
//    private final Map<String, TestImage> testItems;

    TestType(String bundleKey, byte signal) {
        this.bundleKey = bundleKey;
        this.signal = signal;
//        this.testItems = testItems;
    }

    public static TestType fromByte(byte b) {
        return values()[b & 0xff];
    }

//    public Map<String, TestImage> getTestItems() {
//        return testItems;
//    }

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
        // TODO: test type
        if (this == SNELLEN_CHART) {
            test = null;
        } else if (this == C_CHART) {
            test = CTest.CTEST;
        } else if (this == E_CHART) {
            test = null;
        } else if (this == STD_LOG_CHART) {
            test = StdLogTest.STD_LOG_TEST;
        } else {
            throw new TestTypeException("Unexpected test type. ");
        }
        return test;
    }

    @Override
    public String toString() {
        if (this == SNELLEN_CHART) {
            return "snellen";
        } else if (this == C_CHART) {
            return "c";
        } else if (this == E_CHART) {
            return "e";
        } else if (this == STD_LOG_CHART) {
            return "stdLog";
        } else {
            throw new TestTypeException("Unexpected test type. ");
        }
    }

    public static TestType fromString(String s) {
        switch (s) {
            case "snellen":
                return SNELLEN_CHART;
            case "c":
                return C_CHART;
            case "e":
                return E_CHART;
            case "stdLog":
                return STD_LOG_CHART;
            default:
                throw new TestTypeException("Unexpected test type " + s);
        }
    }
}
