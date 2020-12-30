package main;

import common.EventLogger;
import dvaScreen.ScreenApp;
import dvaTest.TestApp;

public class Main {

    /**
     * 主函数
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        if (args.length < 1) {
            EventLogger.log("Too few arguments");
            return;
        }
        String mode = args[0];
        if (mode.equals("-t")) {
            TestApp.run(args);
        } else if (mode.equals("-s")) {
            ScreenApp.run(args);
        } else {
            EventLogger.log("Unexpected mode '" + mode + "'");
        }
    }
}
