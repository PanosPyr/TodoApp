package com.example.todoapp.controller;

import com.example.todoapp.dto.TaskDTO;
import com.example.todoapp.managers.TaskList;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TodoController {

    @FXML
    public MFXComboBox<String> statusComboBox;
    @FXML
    public VBox taskListVBox;

    private TaskList taskList;

    public void initialize(){
        taskList = new TaskList();
        statusComboBox.getItems().addAll("All", "ToDo", "In Progress", "Done");
        statusComboBox.setValue("All");
        statusComboBox.valueProperty().addListener(
                new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                        filterTasksByStatus(newValue);
                    }
                }
        );

        redrawTaskList();

    }

    private void filterTasksByStatus(String status){
        taskListVBox.getChildren().clear();

        List<TaskDTO> filteredTasks;

        if("All".equals(status)){
            filteredTasks = taskList.getTasks();
        }
        else{
            filteredTasks = taskList.getTasks().stream().filter(
                    task -> task.getStatus().equals(status)).collect(Collectors.toList());
        }
        for(TaskDTO task : filteredTasks){
            displayTask(task);
        }
    }

    @FXML
    public void handleAddTask(ActionEvent actionEvent) {
        showAddTaskDialog();
    }

    private void showAddTaskDialog(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/todoapp/task_add_dialog.fxml"));
            VBox dialogPane = loader.load();

            TaskAddDialogController dialogController = loader.getController();
            dialogController.setMainController(this);

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Add New Task");
            dialogStage.initModality(Modality.APPLICATION_MODAL);

            Scene scene = new Scene(dialogPane);
            String css = Objects.requireNonNull(this.getClass().getResource("/com/example/todoapp/addtaskstyles.css")).toExternalForm();
            scene.getStylesheets().add(css);
            dialogStage.setScene(scene);
            dialogStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addTaskFromDialog(String title, String description){
        addTask(title, description, LocalDateTime.now(), "ToDo");
    }

    private void addTask(String taskName, String description, LocalDateTime timestamp, String status){
        TaskDTO newTask = new TaskDTO(taskName, description, timestamp, status);
        taskList.addTask(newTask);
        redrawTaskList();
    }

    private void displayTask(TaskDTO task){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/todoapp/task-card.fxml"));

            HBox taskCard = loader.load();
            TaskCardController controller = loader.getController();
            controller.setTaskList(taskList);
            controller.setTaskDetails(task.getTitle(), task.getDateAdded(), task.getStatus(), task.getId(), this);
            taskListVBox.getChildren().add(taskCard);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void redrawTaskList(){
        taskListVBox.getChildren().clear();

        for(TaskDTO task: taskList.getTasks()){
            displayTask(task);
        }
        statusComboBox.clearSelection();
        statusComboBox.setValue("All");
    }

}
