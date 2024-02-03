package com.example.matrixsystem.controllers;

import com.example.matrixsystem.beans.DatabaseManager;
import com.example.matrixsystem.dto.TaskDTO;
import com.example.matrixsystem.exceptions.NoSuchModuleInDB;
import com.example.matrixsystem.exceptions.NoSuchTaskInDB;
import com.example.matrixsystem.spring_data.entities.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/api/v1")
public class DatabaseController {
    private final DatabaseManager manager;
    @Autowired
    public DatabaseController(DatabaseManager manager){
        this.manager = manager;
    }


    @GetMapping("/module/{num}")
    public List<Integer> allTasksIds(@PathVariable Integer num){
        return manager.getAllModuleTasksIds(num);
    }
    @GetMapping("/module/{moduleNum}/task/{taskNum}")
    public ResponseEntity<Object> getTaskId(@PathVariable Integer moduleNum, @PathVariable Integer taskNum){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(manager.getAllModuleTasks(moduleNum).get(taskNum-1).getId());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Модуль с таким id не найден " +
                    "/ задания с таким номером не существует");
        }
    }
    @GetMapping("/task/{taskId}")
    public ResponseEntity<Object> getTaskById(@PathVariable Integer taskId){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(manager.getTaskById(taskId));
        }catch (NoSuchTaskInDB e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @PostMapping("/task")
    public ResponseEntity<String> addNewTaskToTheModule(@RequestBody TaskDTO taskDTO){
        Task task = new Task();
        try{
            task.setModule(manager.getModuleById(taskDTO.getModule()));
        }catch (NoSuchModuleInDB noSuchModuleInDB){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(noSuchModuleInDB.getMessage());
        }
        task.setTask(taskDTO.getTask());
        task.setImg(taskDTO.getImg());
        task.setAnswer(taskDTO.getAnswer());
        manager.addNewTask(task);
        return ResponseEntity.status(HttpStatus.CREATED).body("Задание успешно добавлено");
    }
}
