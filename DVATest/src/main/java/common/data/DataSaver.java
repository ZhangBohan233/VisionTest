package common.data;

import common.EventLogger;
import dvaTest.gui.items.ScoreCounting;
import dvaTest.testCore.ResultRecord;
import dvaTest.testCore.TestPref;
import dvaTest.testCore.TestType;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DataSaver {

    public static final String DATA_DIR = "data";
    private static final SimpleDateFormat NAME_FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH-mm");
    private static final SimpleDateFormat TIME_FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void saveTestResult(ResultRecord.NamedRecord record) {
        createDirsIfNone();

        File subjectDir = new File(DATA_DIR + File.separator + record.name + ".json");
        if (!subjectDir.exists()) {
            if (!subjectDir.mkdirs()) throw new RuntimeException("Failed to create dir " + subjectDir);
        }

        Date testDate = new Date(System.currentTimeMillis());
        String nameDateStr = NAME_FORMATTER.format(testDate);
        String dateStr = TIME_FORMATTER.format(testDate);

        JSONObject base = new JSONObject();
        base.put("name", record.name);
        base.put("time", dateStr);
        base.put("type", record.resultRecord.testPref.getTestType().toString());
        base.put("distance", record.resultRecord.testPref.getDistance());
        base.put("scoreCounting", record.resultRecord.testPref.getScoreCounting().name());
        base.put("interval", record.resultRecord.testPref.getIntervalMills());
        base.put("note", record.note);

        JSONArray resultArray = new JSONArray();
        for (ResultRecord.RecordUnit ru : record.resultRecord.recordUnits) {
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

    public static ResultRecord.NamedRecord loadSavedResult(File file) {
        try {
            String content;
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            StringBuilder builder = new StringBuilder();
            while ((line = br.readLine()) != null) {
                builder.append(line);
            }
            br.close();
            content = builder.toString();

            JSONObject root = new JSONObject(content);

            String name = root.getString("name");
            String timeStr = root.getString("time");
            String typeStr = root.getString("type");
            String scoreCountingStr = root.getString("scoreCounting");
            double distance = root.getDouble("distance");
            long interval = root.getLong("interval");
            String note = "";
            if (root.has("note")) {
                note = root.getString("note");
            }
            JSONArray results = root.getJSONArray("results");
            List<ResultRecord.RecordUnit> recordUnits = new ArrayList<>();
            for (Object obj : results) {
                JSONObject json = (JSONObject) obj;
                String vision = json.getString("vision");
                String shown = json.getString("shown");
                String input = json.getString("input");
                boolean correct = json.getBoolean("correct");
                recordUnits.add(new ResultRecord.RecordUnit(vision, shown, input, correct));
            }
            TestPref testPref = new TestPref.TestPrefBuilder()
                    .testType(TestType.fromString(typeStr))
                    .scoreCounting(ScoreCounting.valueOf(scoreCountingStr))
                    .distance(distance)
                    .frameTimeMills(interval)
                    .build();
            ResultRecord rr = new ResultRecord(recordUnits, testPref);
            return new ResultRecord.NamedRecord(rr, name, note, TIME_FORMATTER.parse(timeStr));
        } catch (IOException | ParseException | JSONException | IllegalArgumentException e) {
            // Record file damaged
            return null;
        } catch (Exception e) {
            // unexpected problem
            e.printStackTrace();
            EventLogger.log(e);
            return null;
        }
    }

    private static void createDirsIfNone() {
        File f = new File(DATA_DIR);
        if (!f.exists()) {
            if (!f.mkdirs()) throw new RuntimeException("Failed to create dir " + f);
        }
    }
}
