package common.data;

import dvaTest.testCore.ResultRecord;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DataSaver {

    public static final String DATA_DIR = "data";
    private static final SimpleDateFormat NAME_FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH-mm");
    private static final SimpleDateFormat TIME_FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void saveTestResult(String subjectName, ResultRecord resultRecord, String note) {
        createDirsIfNone();

        File subjectDir = new File(DATA_DIR + File.separator + subjectName);
        if (!subjectDir.exists()) {
            if (!subjectDir.mkdirs()) throw new RuntimeException("Failed to create dir " + subjectDir);
        }

        Date testDate = new Date(System.currentTimeMillis());
        String nameDateStr = NAME_FORMATTER.format(testDate);
        String dateStr = TIME_FORMATTER.format(testDate);

        JSONObject base = new JSONObject();
        base.put("name", subjectName);
        base.put("time", dateStr);
        base.put("type", resultRecord.testPref.getTestType().toString());
        base.put("distance", resultRecord.testPref.getDistance());
        base.put("scoreCounting", resultRecord.testPref.getScoreCounting().name());
        base.put("interval", resultRecord.testPref.getIntervalMills());
        base.put("note", note);

        JSONArray resultArray = new JSONArray();
        for (ResultRecord.RecordUnit ru : resultRecord.recordUnits) {
            JSONObject obj = new JSONObject();
            obj.put("vision", ru.getVisionLevel());
            obj.put("shown", ru.getShown());
            obj.put("input", ru.getUserInput());
            obj.put("correct", ru.isCorrect());
            resultArray.put(obj);
        }

        base.put("results", resultArray);

        String jsonString = base.toString(2);
        String fileName = subjectDir.getAbsolutePath() + File.separator + "test-" + nameDateStr;

        try {
            FileWriter fileWriter = new FileWriter(fileName);
            fileWriter.write(jsonString);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void createDirsIfNone() {
        File f = new File(DATA_DIR);
        if (!f.exists()) {
            if (!f.mkdirs()) throw new RuntimeException("Failed to create dir " + f);
        }
    }
}
