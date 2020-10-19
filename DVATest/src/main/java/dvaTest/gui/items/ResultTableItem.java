package dvaTest.gui.items;

import common.Utility;
import javafx.fxml.FXML;

public class ResultTableItem implements Comparable<ResultTableItem> {

    private final double visionLevel;
    private final int correctCount;
    private final int incorrectCount;

    public ResultTableItem(double visionLevel, int correctCount, int incorrectCount) {
        this.visionLevel = visionLevel;
        this.correctCount = correctCount;
        this.incorrectCount = incorrectCount;
    }

    @Override
    public int compareTo(ResultTableItem o) {
        return Double.compare(visionLevel, visionLevel);
    }

    @FXML
    public double getVisionLevel() {
        return visionLevel;
    }

    @FXML
    public int getCorrectCount() {
        return correctCount;
    }

    @FXML
    public int getIncorrectCount() {
        return incorrectCount;
    }

    @FXML
    public String getCorrectRatio() {
        return Utility.round((double) correctCount * 100 / (correctCount + incorrectCount), 2) + "%";
    }
}
