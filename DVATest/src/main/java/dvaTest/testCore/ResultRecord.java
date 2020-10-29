package dvaTest.testCore;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ResultRecord {

    public final List<RecordUnit> recordUnits;
    public final TestPref testPref;

    public ResultRecord(List<RecordUnit> recordUnits, TestPref testPref) {
        this.recordUnits = recordUnits;
        this.testPref = testPref;
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

        public static RecordUnit fromTest(TestController.TestResultUnit resultUnit) {
            return new RecordUnit(
                    resultUnit.getTestUnit().getVisionLevel(),
                    resultUnit.getTestUnit().getTestImage().getName(),
                    resultUnit.getUserInput(),
                    resultUnit.isCorrect()
            );
        }

        /**
         * 将测试结果列表转换为Map
         *
         * @return map of {level: [correct, incorrect]}
         */
        public static Map<String, int[]> recordListToLevelMap(List<RecordUnit> resultUnitList) {
            Map<String, int[]> sucFailMap = new TreeMap<>();  // vision level: [correct, incorrect]
            for (RecordUnit ru: resultUnitList) {
                int[] res = sucFailMap.computeIfAbsent(ru.getVisionLevel(), k -> new int[2]);
                if (ru.isCorrect()) res[0]++;
                else res[1]++;
            }
            return sucFailMap;
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

    public static class NamedRecord {

        public final ResultRecord resultRecord;
        public final String name;
        public final String note;
        public final Date creationTime;

        public NamedRecord(ResultRecord resultRecord, String name, String note) {
            this.resultRecord = resultRecord;
            this.name = name;
            this.note = note;
            this.creationTime = new Date(System.currentTimeMillis());
        }

        public NamedRecord(ResultRecord resultRecord, String name, String note, Date creationTime) {
            this.resultRecord = resultRecord;
            this.name = name;
            this.note = note;
            this.creationTime = creationTime;
        }
    }
}
