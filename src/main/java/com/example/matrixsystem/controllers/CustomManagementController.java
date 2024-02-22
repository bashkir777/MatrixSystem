package com.example.matrixsystem.controllers;

import com.example.matrixsystem.beans.CustomDatabaseManager;
import com.example.matrixsystem.dto.CustomTaskDTO;
import com.example.matrixsystem.dto.TaskForAddingDTO;
import com.example.matrixsystem.security.annotations.RolesAllowed;
import com.example.matrixsystem.security.beans.UserInformation;
import com.example.matrixsystem.spring_data.entities.CustomTask;
import com.example.matrixsystem.spring_data.exceptions.NoSuchHomeworkException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/management")
public class CustomManagementController {
    private final CustomDatabaseManager manager;
    private final UserInformation userInformation;

    @Autowired
    public CustomManagementController(CustomDatabaseManager manager, UserInformation userInformation) {
        this.manager = manager;
        this.userInformation = userInformation;
    }
    @PostMapping("/add/task/custom-task")
    @RolesAllowed({"GOD", "TEACHER"})
    public ResponseEntity<String> addNewCustomTask(@RequestBody TaskForAddingDTO taskForAddingDTO) {

        // !!!здесь будет логика для добавления кастомного задания

        return ResponseEntity.status(HttpStatus.CREATED).body("Задание успешно добавлено");
    }

    @GetMapping("/homework/{id}")
    public List<CustomTaskDTO> getHomework(@PathVariable Integer id) throws NoSuchHomeworkException {
        List<CustomTaskDTO> customTaskDTOS = new ArrayList<>();
        List<CustomTask> customTaskList = manager.getAllCustomTasksOfHomework(manager.getHomeworkById(id));
        customTaskList.forEach(t->
            customTaskDTOS.add(CustomTaskDTO.builder().task(t.getTask())
                    .solution(t.getSolution()).answer(t.getAnswer())
                    .img(t.getImg()).id(t.getId()).build())
        );
        return customTaskDTOS;
    }
}
