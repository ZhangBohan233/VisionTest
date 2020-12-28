package dvaTest.testCore;

import common.Utility;
import common.data.DataSaver;
import dvaTest.gui.items.ScoreCounting;
import dvaTest.testCore.tests.Test;

import java.util.*;

public class ResultRecord {
    public final Map<EyeSide, UnitList[]> testResults;
    public final TestPref testPref;
    public final Date testStartTime;
    public final String fileName;

    public ResultRecord(Map<EyeSide, UnitList[]> testResults, TestPref testPref, Date testStartTime) {
        this.testResults = testResults;
        this.testPref = testPref;
        this.testStartTime = testStartTime;
        this.fileName = "test-" + DataSaver.FILE_NAME_FORMATTER.format(testStartTime) + ".json";
    }

    /**
     * 将测试结果列表转换为Map
     *
     * @return map of {level: [correct, incorrect]}
     */
    public Map<EyeSide, Map<String, int[]>> toLevelMap(Test test, ScoreCounting src, ScoreCounting dst) {
        Map<EyeSide, Map<String, int[]>> sideMap = new TreeMap<>();
        for (Map.Entry<EyeSide, UnitList[]> entry : testResults.entrySet()) {
            Map<String, int[]> sucFailMap = new TreeMap<>();  // vision level: [correct, incorrect]
            for (UnitList ul : entry.getValue()) {
                for (RecordUnit ru : ul) {
                    int[] res = sucFailMap.computeIfAbsent(
                            src == dst ? ru.getVisionLevel() :
                                    Utility.transferScoreCounting(test, src, ru.getVisionLevel(), dst),
                            k -> new int[2]);
                    if (ru.isCorrect()) res[0]++;
                    else res[1]++;
                }
            }
            sideMap.put(entry.getKey(), sucFailMap);
        }
        return sideMap;
    }

    public Map<EyeSide, String> generateScoreConclusions(ScoreCounting scoreCounting) {
        Map<EyeSide, String> result = new TreeMap<>();
        for (Map.Entry<EyeSide, UnitList[]> entry : testResults.entrySet()) {
            result.put(entry.getKey(), generateSideScoreConclusion(entry.getValue(), scoreCounting));
        }
        return result;
    }

    /**
     * 产生单个评分
     *
     * @return the string conclusion of score
     */
    private String generateSideScoreConclusion(UnitList[] sideResults, ScoreCounting scoreCounting) {
        int levelCount = testPref.getTestType().getTest().visionLevelCount();
        int highestIndex = 0;
        int failsInHighest = 0;
        List<Integer> corrInFailedLevels = new ArrayList<>();
        for (int i = 0; i < levelCount; i++) {
            UnitList ul = sideResults[i];
            int corrCount = ul.correctCount();
            if (corrCount > ul.size() / 2) {
                highestIndex = i;
                failsInHighest = ul.size() - corrCount;
            } else if (corrCount > 0) {
                corrInFailedLevels.add(corrCount);
            }
        }
        StringBuilder stringBuilder =
                new StringBuilder(testPref.getTestType().getTest().getLevelString(
                        scoreCounting,
                        highestIndex));
        if (failsInHighest > 0) stringBuilder.append(" - ").append(failsInHighest);
        for (int i : corrInFailedLevels) stringBuilder.append(" + ").append(i);
        return stringBuilder.toString();
    }

    public static class RecordUnit {

        private final String visionLevel;
        private final String shown;
        private final String userInput;
        private final boolean correct;

        public RecordUnit(String visionLevel, String shown, String userInput, boolean correct) {
            this.visionLevel = visionLevel;
            this.shown = shown;
            this.userInput = userInput;
            this.correct = correct;
        }

        public String getVisionLevel() {
            return visionLevel;
        }

        public boolean isCorrect() {
            return correct;
        }

        public String getShown() {
            return shown;
        }

        public String getUserInput() {
            return userInput;
        }
    }

    public static class UnitList extends ArrayList<RecordUnit> {
        public int correctCount() {
            int corrCount = 0;
            for (ResultRecord.RecordUnit tru : this) {
                if (tru.isCorrect()) corrCount++;
            }
            return corrCount;
        }
    }

    public static class NamedRecord {

        public final ResultRecord resultRecord;
        public final String name;
        public final String note;

        public NamedRecord(ResultRecord resultRecord, String name, String note) {
            this.resultRecord = resultRecord;
            this.name = name;
            this.note = note;
        }
    }
}
