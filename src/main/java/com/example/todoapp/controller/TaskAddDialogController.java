package com.example.todoapp.controller;

import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class TaskAddDialogController {

    @FXML
    public MFXTextField taskTitleField;

    @FXML
    public TextArea taskDescriptionField;

    private TodoController mainController;

    public void setMainController(TodoController mainController){
        this.mainController = mainController;
    }

    @FXML
    public void handleCancel(ActionEvent actionEvent) {
        closeDialog();
    }

    @FXML
    public void handleSubmit(ActionEvent actionEvent) {
        if(!taskTitleField.getText().isEmpty()){
            mainController.addTaskFromDialog(taskTitleField.getText(), taskDescriptionField.getText());
            closeDialog();
        }
        else{
            Alert titleAlert = new Alert(Alert.AlertType.ERROR, "Title is required");
            titleAlert.showAndWait();
        }

    }

    private void closeDialog(){
        Stage stage = (Stage) taskDescriptionField.getScene().getWindow();
        stage.close();
    }
}
