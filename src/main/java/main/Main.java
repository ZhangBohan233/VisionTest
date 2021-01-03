package main;

import common.EventLogger;
import dvaScreen.ScreenApp;
import dvaTest.TestApp;

public class Main {

    private static final String USAGE =
            "Usage: javaw -jar dva-test.jar -mode\n " +
                    "modes: \n" +
                    "    -t, -i : test app\n" +
                    "    -s     : screen app";

    /**
     * 主函数
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println(USAGE);
            EventLogger.log("Too few arguments");
            return;
        }
        String mode = args[0];
        switch (mode) {
            case "-t":
            case "-i":
                TestApp.run(args);
                break;
            case "-s":
                ScreenApp.run(args);
                break;
            default:
                System.out.println(USAGE);
                EventLogger.log("Unexpected mode '" + mode + "'");
                break;
        }
    }
}
