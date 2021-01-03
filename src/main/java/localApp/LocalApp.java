package localApp;

import dvaScreen.ScreenApp;
import dvaTest.TestApp;

/**
 * 一个完全不使用网络的版本
 */
public class LocalApp {

    public static void startLocalApp(String[] args) {
        ScreenApp.runLocal(args);
        TestApp.runLocal(args);
    }
}
