package common;

public class Utility {

    public static void doubleToBytes(double value, byte[] array, int index) {
        long l = Double.doubleToLongBits(value);
        for (int i = 0; i < 8; i++) {
            array[index + i] = (byte) ((l >> 8 * i) & 0xff);
        }
    }

    public static double bytesToDouble(byte[] array, int index) {
        long v = 0;
        for (int i = 0; i < 8; i++) {
            v |= (array[index + i] & 0xff) << (8 * i);
        }
        return Double.longBitsToDouble(v);
    }

    private static int digitMultiplier(int digit) {
        int multiplier = 1;
        for (int i = 0; i < digit; i++) {
            multiplier *= 10;
        }
        return multiplier;
    }

    public static double round(double value, int digit) {
        int multiplier = digitMultiplier(digit);
        return (double) Math.round(value * multiplier) / multiplier;
    }
}
