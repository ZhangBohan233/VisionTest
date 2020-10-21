package common;

public class Utility {

    public static final char[] ILLEGAL_NAME_CHARS = {'\\', '/', ':', '*', '?', '"', '<', '>', '|'};

    public static void doubleToBytes(double value, byte[] array, int index) {
        long l = Double.doubleToLongBits(value);
        for (int i = 0; i < 8; i++) {
            array[index + i] = (byte) ((l >> 8 * i) & 0xff);
        }
    }

    public static double bytesToDouble(byte[] array, int index) {
        long v = 0;
        for (int i = 0; i < 8; i++) {
            v |= ((long) (array[index + i] & 0xff)) << (8 * i);
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

    public static boolean isValidFileName(String fileName) {
        for (char c : ILLEGAL_NAME_CHARS) {
            if (fileName.indexOf(c) >= 0) return false;
        }
        return fileName.length() > 0;
    }

    private static void calculateLogVisions() {
        double d = 7.272;
        double f = 1.0;
        final double inc = Math.pow(10, 0.1);
        for (int i = 0; i <= 10; i++) {
            System.out.println(d + " " + f);
            d *= inc;
            f *= inc;
        }
        System.out.println("====");
        d = 7.272;
        f = 1.0;
        for (int i = 0; i <= 3; i++) {
            System.out.println(d + " " + f);
            d /= inc;
            f /= inc;
        }
    }

    public static void main(String[] args) {
        byte[] arr = new byte[8];
        doubleToBytes(1.0, arr, 0);

        System.out.println(bytesToDouble(arr, 0));

        calculateLogVisions();
    }
}
