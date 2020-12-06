package common.data;

import common.EventLogger;
import dvaTest.TestApp;
import dvaTest.gui.items.ScoreCounting;
import dvaTest.testCore.EyeSide;
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
import java.util.*;

public class DataSaver {

    public static final String DATA_DIR = "data";
    public static final SimpleDateFormat FILE_NAME_FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH-mm");
    private static final SimpleDateFormat TIME_FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * @param personName 被试姓名
     * @return 该被试的文件夹
     */
    public static File getSubjectDirByPerson(String personName) {
        return new File(DATA_DIR + File.separator + personName);
    }

    /**
     * 保存测试记录
     *
     * @param record 测试记录
     * @return 若保存成功，返回{@code true}。反之返回{@code false}
     */
    public static boolean saveTestResult(ResultRecord.NamedRecord record) {
        createDirsIfNone();

        File subjectDir = getSubjectDirByPerson(record.name);
        if (!subjectDir.exists()) {
            if (!subjectDir.mkdirs()) {
                EventLogger.log(new RuntimeException("Failed to create dir " + subjectDir));
                return false;
            }
        }

        Date testDate = new Date(System.currentTimeMillis());
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
//        base.put("conclusion", record.resultRecord.scoreConclusion);
        base.put("note", record.note);

        JSONObject results = new JSONObject();

        for (Map.Entry<EyeSide, ResultRecord.UnitList[]> entry : record.resultRecord.testResults.entrySet()) {
            JSONArray resultArray = new JSONArray();
            for (ResultRecord.UnitList ul : entry.getValue()) {
                for (ResultRecord.RecordUnit ru : ul) {
                    JSONObject obj = new JSONObject();
                    obj.put("vision", ru.getVisionLevel());
                    obj.put("shown", ru.getShown());
                    obj.put("input", ru.getUserInput());
                    obj.put("correct", ru.isCorrect());
                    resultArray.put(obj);
                }
            }
            JSONObject side = new JSONObject();
            side.put("conclusion", record.resultRecord.scoreConclusions.get(entry.getKey()));
            side.put("detail", resultArray);
            results.put(entry.getKey().name(), side);
        }

        base.put("results", results);

        String jsonString = base.toString(2);
        String fileName = createFileNameNoDup(
                subjectDir.getAbsolutePath() + File.separator + record.resultRecord.fileName);

        try {
            FileWriter fileWriter = new FileWriter(fileName);
            fileWriter.write(jsonString);
            fileWriter.flush();
            fileWriter.close();
            return true;
        } catch (IOException e) {
            EventLogger.log(e);
            return false;
        }
    }

    private static String createFileNameNoDup(String oriFileName) {
        String curName = oriFileName;
        int count = 0;
        while (new File(curName).exists()) {
            curName = String.format("%s(%d)", oriFileName, ++count);
        }
        return curName;
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
            Map<EyeSide, ResultRecord.UnitList[]> resultMap = new TreeMap<>();
            JSONObject results = root.getJSONObject("results");

            for (String enumName : results.keySet()) {
                JSONObject side = results.getJSONObject(enumName);
                JSONArray detail = side.getJSONArray("detail");

                ResultRecord.UnitList[] unitLists = new ResultRecord.UnitList[vlcCount];
                for (int i = 0; i < vlcCount; i++) unitLists[i] = new ResultRecord.UnitList();
                for (Object obj : detail) {
                    JSONObject json = (JSONObject) obj;
                    String vision = json.getString("vision");
                    String shown = json.getString("shown");
                    String input = json.getString("input");
                    boolean correct = json.getBoolean("correct");
                    ResultRecord.RecordUnit ru = new ResultRecord.RecordUnit(vision, shown, input, correct);
                    int levelIndex = testType.getTest().getLevelIndexFromShown(scoreCounting, vision);
                    unitLists[levelIndex].add(ru);
                }
                resultMap.put(EyeSide.valueOf(enumName), unitLists);
            }

            ResultRecord rr = new ResultRecord(resultMap, testPref, TIME_FORMATTER.parse(timeStr));
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
                bundle.getString("leftEye"),
                bundle.getString("rightEye"),
                bundle.getString("bothEyes"),
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
            row.createCell(8).setCellValue(record.resultRecord.scoreConclusions.get(EyeSide.LEFT));
            row.createCell(9).setCellValue(record.resultRecord.scoreConclusions.get(EyeSide.RIGHT));
            row.createCell(10).setCellValue(record.resultRecord.scoreConclusions.get(EyeSide.BOTH));
            row.createCell(11).setCellValue(record.note);
        }

        FileOutputStream fos = new FileOutputStream(file);
        workbook.write(fos);
        fos.flush();
        fos.close();
    }

    /**
     * @param recordList 将要删除的项目列表
     * @return 成功删除项目的数量
     */
    public static int deleteRecords(List<ResultRecord.NamedRecord> recordList) {
        int sucCount = 0;
        for (ResultRecord.NamedRecord nr : recordList) {
            File file = new File(getSubjectDirByPerson(nr.name) + File.separator + nr.resultRecord.fileName);
            if (file.delete()) {
                sucCount++;
            } else {
                System.out.println(file + " deletion failed.");
            }
        }
        return sucCount;
    }
}
