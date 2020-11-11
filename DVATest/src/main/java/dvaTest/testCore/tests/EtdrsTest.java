package dvaTest.testCore.tests;

import dvaTest.testCore.TestType;
import dvaTest.testCore.testItems.TestImage;

import java.util.Map;

public class EtdrsTest extends LogBasedText {

    public final static Map<String, TestImage> ITEMS = Map.of(
            "C", new TestImage("C", "etdrs/C.bmp", TestType.ETDRS),
            "D", new TestImage("D", "etdrs/D.bmp", TestType.ETDRS),
            "H", new TestImage("H", "etdrs/H.bmp", TestType.ETDRS),
            "K", new TestImage("K", "etdrs/K.bmp", TestType.ETDRS),
            "N", new TestImage("N", "etdrs/N.bmp", TestType.ETDRS),
            "O", new TestImage("O", "etdrs/O.bmp", TestType.ETDRS),
            "R", new TestImage("R", "etdrs/R.bmp", TestType.ETDRS),
            "S", new TestImage("S", "etdrs/S.bmp", TestType.ETDRS),
            "V", new TestImage("V", "etdrs/V.bmp", TestType.ETDRS),
            "Z", new TestImage("Z", "etdrs/Z.bmp", TestType.ETDRS)
    );

    public static final EtdrsTest ETDRS_TEST = new EtdrsTest(ITEMS);

    public EtdrsTest(Map<String, TestImage> testImageMap) {
        super(testImageMap);
    }

    @Override
    public double standardHeightMm(double distance) {
        return 7.272 / 5 * distance;
    }
}
