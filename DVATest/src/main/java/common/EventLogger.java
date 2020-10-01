package common;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EventLogger {

    private static final String LOG_BASE_NAME = "error-";

    public static void log(Throwable throwable) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
            String realName = LOG_BASE_NAME + sdf.format(new Date(System.currentTimeMillis())) + ".log";
            FileWriter fileWriter = new FileWriter(realName);
            PrintWriter pw = new PrintWriter(fileWriter);
            throwable.printStackTrace(pw);

            pw.flush();
            pw.close();
        } catch (IOException e) {
            //
        }
    }
}
