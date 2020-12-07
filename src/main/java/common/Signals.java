package common;

public class Signals {
    public static final byte GREET = (byte) 233;  // -23
    public static final byte DISCONNECT_BY_CLIENT = -1;
    public static final byte SCREEN_INTERRUPT = -2;
    public static final byte STOP_TEST = -3;
    public static final byte DISCONNECT_BY_SERVER = -4;
    public static final byte SHOW_SNELLEN = 10;
    public static final byte SHOW_E = 11;
    public static final byte SHOW_C = 12;
    public static final byte SHOW_STD_LOG = 13;
    public static final byte SHOW_ETDRS = 14;
    public static final byte NEXT_TEST_UNIT = 20;
    public static final byte TEST_CONNECTION = 30;
    public static final byte SHOW_EYE_SIDE = 40;
}
