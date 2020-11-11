package dvaTest.testCore;

import common.Signals;
import dvaTest.testCore.tests.*;

import java.util.ResourceBundle;

public enum TestType {
    SNELLEN("snellenChart", Signals.SHOW_SNELLEN),
    LANDOLT("cChart", Signals.SHOW_C),
    E_CHART("eChart", Signals.SHOW_E),
    ETDRS("etdrsChart", Signals.SHOW_ETDRS),
    STD_LOG("stdLogChart", Signals.SHOW_STD_LOG);

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
        if (this == SNELLEN) {
            test = SnellenTest.SNELLEN_TEST;
        } else if (this == LANDOLT) {
            test = CTest.CTEST;
        } else if (this == E_CHART) {
            throw new RuntimeException();
        } else if (this == STD_LOG) {
            test = StdLogTest.STD_LOG_TEST;
        } else if (this == ETDRS) {
            test = EtdrsTest.ETDRS_TEST;
        } else {
            throw new TestTypeException("Unexpected test type. ");
        }
        return test;
    }
}
