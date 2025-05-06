package com.example.todoapp.controller;

import com.example.todoapp.dto.TaskDTO;
import com.example.todoapp.managers.TaskList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class TaskCardController {

    @FXML
    public Label taskName;
    @FXML
    public Label taskTimesStamp;
    @FXML
    public Label taskStatus;

    public String taskId;
    private TaskList taskList = new TaskList();
    private TodoController mainController;


    @FXML
    private void handleViewTask() {
        TaskDTO task = taskList.getTaskById(taskId);
        showViewTaskDialog(task);
    }

    // Added method to open the dialog box to view task
    private void showViewTaskDialog(TaskDTO task) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/todoapp/task_view_dialog.fxml"));
            VBox dialogPane = loader.load();

            TaskViewDialogController dialogController = loader.getController();
            dialogController.setTaskDetails(task, this);

            Stage dialogStage = new Stage();
            dialogStage.setTitle("View Task");
            dialogStage.initModality(Modality.APPLICATION_MODAL);

            Scene scene = new Scene(dialogPane);

            String css = Objects.requireNonNull(this.getClass().getResource("/com/example/todoapp/viewdialogstyles.css")).toExternalForm();
            scene.getStylesheets().add(css);

            dialogStage.setScene(scene);
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setTaskDetails(String name, LocalDateTime timestamp, String status, String id, TodoController controller) {
        taskName.setText(name);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a, MM/dd/yyyy");
        taskTimesStamp.setText(timestamp.format(formatter));
        taskStatus.setText(status);
        this.taskId = id; // Id has been added
        applyStatusColor(status);// Changing color based on status

        mainController = controller;
    }

    private void applyStatusColor(String status){
        switch(status){
            case "ToDo":
                taskStatus.setStyle("-fx-text-fill: grey;");
                break;
            case "In Progress":
                taskStatus.setStyle("-fx-text-fill: orange;");
                break;
            case "Done":
                taskStatus.setStyle("-fx-text-fill: green;");
                break;
            default:
                taskStatus.setStyle("-fx-text-fill: black;");
                break;
        }

    }

    public void setTaskList(TaskList taskList){
        this.taskList = taskList;
    }

    public void updateTask(TaskDTO task){
        taskList.updateTask(task);
        taskName.setText(task.getTitle());
        taskStatus.setText(task.getStatus());
        applyStatusColor(task.getStatus());

        mainController.redrawTaskList();
    }

    public void deleteTask(TaskDTO task){
        taskList.removeTask(task);
        mainController.redrawTaskList();
    }
}
