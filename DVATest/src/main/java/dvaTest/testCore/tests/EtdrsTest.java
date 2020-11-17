package dvaTest.testCore.tests;

import dvaTest.testCore.TestType;
import dvaTest.testCore.testItems.TestImage;

import java.util.Map;

public class EtdrsTest extends LogBasedText {

    public final static Map<String, TestImage> ITEMS = Map.of(
            "C", new TestImage("C", "etdrs/C.bmp"),
            "D", new TestImage("D", "etdrs/D.bmp"),
            "H", new TestImage("H", "etdrs/H.bmp"),
            "K", new TestImage("K", "etdrs/K.bmp"),
            "N", new TestImage("N", "etdrs/N.bmp"),
            "O", new TestImage("O", "etdrs/O.bmp"),
            "R", new TestImage("R", "etdrs/R.bmp"),
            "S", new TestImage("S", "etdrs/S.bmp"),
            "V", new TestImage("V", "etdrs/V.bmp"),
            "Z", new TestImage("Z", "etdrs/Z.bmp")
    );

    public EtdrsTest() {
        super(ITEMS);
    }

    @Override
    public double standardHeightMm(double distance) {
        return 7.272 / 5 * distance;
    }
}
