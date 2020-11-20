package dvaTest.testCore;

import common.EventLogger;
import common.Signals;
import dvaTest.gui.widgets.inputs.*;
import dvaTest.testCore.tests.*;

import java.lang.reflect.InvocationTargetException;
import java.util.ResourceBundle;

public enum TestType {
    SNELLEN("snellenChart", Signals.SHOW_SNELLEN, SnellenTest.class, SnellenTestInput.class),
    LANDOLT("cChart", Signals.SHOW_C, CTest.class, CTestInput.class),
    ETDRS("etdrsChart", Signals.SHOW_ETDRS, EtdrsTest.class, EtdrsTestInput.class),
    STD_LOG("stdLogChart", Signals.SHOW_STD_LOG, StdLogTest.class, StdLogTestInput.class);

    private final String bundleKey;
    private final byte signal;
    private final Class<? extends Test> testClass;
    private final Class<? extends TestInput> inputClass;
    private Test testInstance;

    TestType(String bundleKey,
             byte signal,
             Class<? extends Test> testClass,
             Class<? extends TestInput> inputClass) {
        this.bundleKey = bundleKey;
        this.signal = signal;
        this.testClass = testClass;
        this.inputClass = inputClass;
    }

    public static TestType fromByte(byte b) {
        return values()[b & 0xff];
    }

    public byte toByte() {
        return (byte) ordinal();
    }

    public byte getSignal() {
        return signal;
    }

    public String show(ResourceBundle bundle, boolean showChartText) {
        return showChartText ?
                bundle.getString(bundleKey) + bundle.getString("visionChart") : bundle.getString(bundleKey);
    }

    public Test getTest() {
        if (testInstance == null) {
            try {
                testInstance = testClass.getDeclaredConstructor().newInstance();
            } catch (InstantiationException |
                    IllegalAccessException |
                    InvocationTargetException |
                    NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
        return testInstance;
    }

    public TestInput generateTestInput(TestController testController) {
        try {
            TestInput testInput = inputClass.getDeclaredConstructor().newInstance();
            testInput.setTestController(testController);
            return testInput;
        } catch (InstantiationException |
                IllegalAccessException |
                InvocationTargetException |
                NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
