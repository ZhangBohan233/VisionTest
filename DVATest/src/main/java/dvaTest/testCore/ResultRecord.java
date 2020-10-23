package dvaTest.testCore;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ResultRecord {

    public final List<RecordUnit> recordUnits;
//    public final TestType testType;
//    public final long intervalMills;
    public final TestPref testPref;

    public ResultRecord(List<RecordUnit> recordUnits, TestPref testPref) {
        this.recordUnits = recordUnits;
        this.testPref = testPref;
//        this.testType = testPref.getTestType();
//        this.intervalMills = testPref.getFrameTimeMills();
    }

    public static class RecordUnit {

        private final double visionLevel;
        private final String shown;
        private final String userInput;
        private final boolean correct;

        private RecordUnit(double visionLevel, String shown, String userInput, boolean correct) {
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
        public static Map<Double, int[]> recordListToLevelMap(List<RecordUnit> resultUnitList) {
            Map<Double, int[]> sucFailMap = new TreeMap<>();  // vision level: [correct, incorrect]
            for (RecordUnit ru: resultUnitList) {
                int[] res = sucFailMap.computeIfAbsent(ru.getVisionLevel(), k -> new int[2]);
                if (ru.isCorrect()) res[0]++;
                else res[1]++;
            }
            return sucFailMap;
        }

        public double getVisionLevel() {
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
}
