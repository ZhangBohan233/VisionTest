package common.data;

import common.EventLogger;
import dvaTest.TestApp;
import dvaTest.gui.items.ScoreCounting;
import dvaTest.testCore.ResultRecord;
import dvaTest.testCore.TestPref;
import dvaTest.testCore.TestType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class DataSaver {

    public static final String DATA_DIR = "data";
    private static final SimpleDateFormat NAME_FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH-mm");
    private static final SimpleDateFormat TIME_FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void saveTestResult(ResultRecord.NamedRecord record) {
        createDirsIfNone();

        File subjectDir = new File(DATA_DIR + File.separator + record.name);
        if (!subjectDir.exists()) {
            if (!subjectDir.mkdirs()) throw new RuntimeException("Failed to create dir " + subjectDir);
        }

        Date testDate = new Date(System.currentTimeMillis());
        String nameDateStr = NAME_FORMATTER.format(testDate);
        String dateStr = TIME_FORMATTER.format(testDate);

        TestPref testPref = record.resultRecord.testPref;

        JSONObject base = new JSONObject();
        base.put("name", record.name);
        base.put("time", dateStr);
        base.put("type", testPref.getTestType().name());
        base.put("distance", testPref.getDistance());
        base.put("scoreCounting", testPref.getScoreCounting().name());
        base.put("interval", testPref.getIntervalMills());
        base.put("hidingTime", testPref.getHidingMills());
        base.put("note", record.note);

        JSONArray resultArray = new JSONArray();
        for (ResultRecord.UnitList ul : record.resultRecord.testResults) {
            for (ResultRecord.RecordUnit ru : ul) {
                JSONObject obj = new JSONObject();
                obj.put("vision", ru.getVisionLevel());
                obj.put("shown", ru.getShown());
                obj.put("input", ru.getUserInput());
                obj.put("correct", ru.isCorrect());
                resultArray.put(obj);
            }
        }

        base.put("results", resultArray);

        String jsonString = base.toString(2);
        String fileName = subjectDir.getAbsolutePath() + File.separator + "test-" + nameDateStr + ".json";

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
            long hidingMills = root.getLong("hidingTime");
            String note = "";
            if (root.has("note")) {
                note = root.getString("note");
            }
            JSONArray results = root.getJSONArray("results");
//            List<ResultRecord.RecordUnit> recordUnits = new ArrayList<>();
//            for (Object obj : results) {
//                JSONObject json = (JSONObject) obj;
//                String vision = json.getString("vision");
//                String shown = json.getString("shown");
//                String input = json.getString("input");
//                boolean correct = json.getBoolean("correct");
//                recordUnits.add(new ResultRecord.RecordUnit(vision, shown, input, correct));
//            }
            TestType testType = TestType.valueOf(typeStr);
            ScoreCounting scoreCounting = ScoreCounting.valueOf(scoreCountingStr);
            TestPref testPref = new TestPref.TestPrefBuilder()
                    .testType(testType)
                    .scoreCounting(scoreCounting)
                    .distance(distance)
                    .frameTimeMills(interval)
                    .hidingTimeMills(hidingMills)
                    .build();

            int vlcCount = testType.getTest().visionLevelCount();
            ResultRecord.UnitList[] unitLists = new ResultRecord.UnitList[vlcCount];
            for (int i = 0; i < vlcCount; i++) unitLists[i] = new ResultRecord.UnitList();
            for (Object obj : results) {
                JSONObject json = (JSONObject) obj;
                String vision = json.getString("vision");
                String shown = json.getString("shown");
                String input = json.getString("input");
                boolean correct = json.getBoolean("correct");
                ResultRecord.RecordUnit ru = new ResultRecord.RecordUnit(vision, shown, input, correct);
                int levelIndex = testType.getTest().getLevelIndexFromShown(scoreCounting, vision);
                unitLists[levelIndex].add(ru);
            }

            ResultRecord rr = new ResultRecord(unitLists, testPref, TIME_FORMATTER.parse(timeStr));
            return new ResultRecord.NamedRecord(rr, name, note);
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

    public static void exportAsXlsx(File file, List<ResultRecord.NamedRecord> recordList) throws IOException {
        ResourceBundle bundle = TestApp.getBundle();
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet();
        XSSFRow titleRow = sheet.createRow(0);

        String[] titles = new String[]{
                bundle.getString("name"),
                bundle.getString("date"),
                bundle.getString("time"),
                bundle.getString("distanceU"),
                bundle.getString("showingTimeU"),
                bundle.getString("hidingTimeU"),
                bundle.getString("testSubject"),
                bundle.getString("visionScoreCount"),
                bundle.getString("testScore"),
                bundle.getString("note")
        };

        // 标题
        for (int i = 0; i < titles.length; i++) {
            XSSFCell cell = titleRow.createCell(i);
            cell.setCellValue(titles[i]);
        }

        // 内容
        for (int i = 0; i < recordList.size(); i++) {
            ResultRecord.NamedRecord record = recordList.get(i);
            TestPref testPref = record.resultRecord.testPref;

            XSSFRow row = sheet.createRow(i + 1);

            row.createCell(0).setCellValue(record.name);
            row.createCell(1).setCellValue(
                    TestApp.getDateFormat().format(record.resultRecord.testStartTime));
            row.createCell(2).setCellValue(
                    TestApp.getTimeFormat().format(record.resultRecord.testStartTime));
            row.createCell(3).setCellValue(testPref.getDistance());
            row.createCell(4).setCellValue((double) testPref.getIntervalMills() / 1000);
            row.createCell(5).setCellValue((double) testPref.getHidingMills() / 1000);
            row.createCell(6).setCellValue(testPref.getTestType().show(bundle, false));
            row.createCell(7).setCellValue(testPref.getScoreCounting().toString());
            row.createCell(8).setCellValue(record.resultRecord.generateScoreConclusion());
            row.createCell(9).setCellValue(record.note);
        }

        FileOutputStream fos = new FileOutputStream(file);
        workbook.write(fos);
        fos.flush();
        fos.close();
    }
}
