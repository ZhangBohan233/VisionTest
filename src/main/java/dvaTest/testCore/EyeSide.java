package dvaTest.testCore;

import common.Signals;
import dvaScreen.ScreenApp;
import dvaTest.TestApp;

import java.util.ResourceBundle;

/**
 * 表示一个眼侧，左眼、右眼、双眼。
 */
public enum EyeSide {
    LEFT("leftEye"),
    RIGHT("rightEye"),
    BOTH("bothEyes");

    private final String showingName;

    EyeSide(String nameKey) {
        ResourceBundle bundle = TestApp.getBundle();
        if (bundle == null) bundle = ScreenApp.getBundle();
        this.showingName = bundle.getString(nameKey);
    }

    public byte[] toBytes() {
        byte[] res = new byte[2];
        res[0] = Signals.SHOW_EYE_SIDE;
        res[1] = (byte) ordinal();
        return res;
    }

    public static EyeSide fromBytes(byte[] bytes) {
        return values()[bytes[1] & 0xff];
    }

    @Override
    public String toString() {
        return showingName;
    }
}
