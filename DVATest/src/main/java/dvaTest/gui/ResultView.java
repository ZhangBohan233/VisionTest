package dvaTest.gui;

import common.Utility;
import common.data.DataSaver;
import dvaScreen.gui.items.ResolutionItem;
import dvaTest.gui.items.ResultTableItem;
import dvaTest.gui.widgets.ResultPane;
import dvaTest.testCore.ResultRecord;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.ResourceBundle;

public class ResultView implements Initializable {

    @FXML
    Pane resultContainer;

    private ResourceBundle bundle;
    private Stage thisStage;
    private boolean saved = false;

    private ResultPane resultPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.bundle = resourceBundle;
    }

    public void setup(Stage stage, ResultRecord resultRecord) {
        this.thisStage = stage;
        resultPane = new ResultPane();
        resultPane.setup(resultRecord);
        resultContainer.getChildren().add(resultPane);
        setOnClose();
    }

    @FXML
    void saveClicked() {
        save(false);
    }

    private void save(boolean closeAfterSave) {
        Stage dialogStage = new Stage();
        dialogStage.initOwner(thisStage);
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initStyle(StageStyle.UTILITY);
        dialogStage.setTitle(bundle.getString("save"));

        Label nameLabel = new Label(bundle.getString("inputSubjectName"));
        TextField nameField = new TextField();
        Label messageLabel = new Label();
        messageLabel.setTextFill(Paint.valueOf("red"));

        Label noteLabel = new Label(bundle.getString("note"));
        TextArea noteArea = new TextArea();

        Button saveButton = new Button(bundle.getString("save"));
        Button cancelButton = new Button(bundle.getString("cancel"));

        saveButton.setOnAction(e -> {
            String subjectName = nameField.getText();
            if (Utility.isValidFileName(subjectName)) {
                DataSaver.saveTestResult(new ResultRecord.NamedRecord(
                        resultPane.getResultRecord(),
                        subjectName,
                        noteArea.getText()));
                saved = true;
                dialogStage.close();
                if (closeAfterSave) {
                    thisStage.close();
                }
            } else {
                messageLabel.setText(bundle.getString("invalidSubjectName"));
            }
        });

        cancelButton.setOnAction(e -> dialogStage.close());

        HBox buttonBox = new HBox(saveButton, cancelButton);
        buttonBox.setSpacing(10.0);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);

        VBox root = new VBox(nameLabel, nameField, noteLabel, noteArea, messageLabel, buttonBox);
        root.setPadding(new Insets(5.0));
        root.setSpacing(10.0);

        dialogStage.setScene(new Scene(root));
        dialogStage.show();
    }

    private void askSave() {
        Stage dialogStage = new Stage();
        dialogStage.setTitle(bundle.getString("save"));
        dialogStage.initStyle(StageStyle.UTILITY);
        dialogStage.initOwner(thisStage);
        dialogStage.initModality(Modality.WINDOW_MODAL);

        Label askSaveLabel = new Label(bundle.getString("doYouWantToSave"));

        Button saveButton = new Button(bundle.getString("save"));
        Button notSaveButton = new Button(bundle.getString("notSave"));

        saveButton.setOnAction(e -> {
            dialogStage.close();
            save(true);
        });

        notSaveButton.setOnAction(e -> {
            dialogStage.close();
            thisStage.close();
        });

        HBox buttonBox = new HBox(saveButton, notSaveButton);
        buttonBox.setSpacing(10.0);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);

        VBox root = new VBox(askSaveLabel, buttonBox);
        root.setPadding(new Insets(5.0));
        root.setSpacing(10.0);

        dialogStage.setScene(new Scene(root));
        dialogStage.showAndWait();
    }

    private void setOnClose() {
        thisStage.setOnCloseRequest(e -> {
            if (!saved) {
                e.consume();
                askSave();
            }
        });
    }
}
