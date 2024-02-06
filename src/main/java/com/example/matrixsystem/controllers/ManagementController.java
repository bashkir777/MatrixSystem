package com.example.matrixsystem.controllers;

import com.example.matrixsystem.beans.DatabaseManager;
import com.example.matrixsystem.dto.TaskDTO;
import com.example.matrixsystem.dto.UserDTO;
import com.example.matrixsystem.exceptions.ErrorCreatingTaskRecord;
import com.example.matrixsystem.exceptions.ErrorCreatingUserRecord;
import com.example.matrixsystem.exceptions.NoSuchModuleInDB;
import com.example.matrixsystem.security.annotations.RolesAllowed;
import com.example.matrixsystem.spring_data.entities.Module;
import com.example.matrixsystem.spring_data.entities.Task;
import com.example.matrixsystem.spring_data.entities.Users;
import com.example.matrixsystem.spring_data.entities.enums.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

// этот контроллер содержит методы, посредством которых осуществляется весь менеджмент
// добавление студентов, учителей, заданий и тп.
@RestController
@RequestMapping("/api/v1")
public class ManagementController {
    private final DatabaseManager manager;

    @Autowired
    public ManagementController(DatabaseManager manager) {
        this.manager = manager;
    }

    @PostMapping("/add/task/common-pull")
    @RolesAllowed("GOD")
    public ResponseEntity<String> addNewTaskToTheCommonPull(@RequestBody TaskDTO taskDTO) {
        Module module;
        try {
            module = manager.getModuleById(taskDTO.getModule());
        } catch (NoSuchModuleInDB noSuchModuleInDB) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(noSuchModuleInDB.getMessage());
        }

        Task task = Task.builder().task(taskDTO.getTask())
                .img(taskDTO.getImg()).answer(taskDTO.getAnswer())
                .module(module).build();

        try{
            manager.addTask(task);
        }catch (ErrorCreatingTaskRecord e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Задание успешно добавлено");

    }

    private ResponseEntity<String> addUserLogic(UserDTO userDTO, Roles role) {
        Users user = Users.builder().login(userDTO.getLogin())
                .role(role).password(userDTO.getPassword()).build();
        try{
            manager.addUser(user);
        }catch (ErrorCreatingUserRecord e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(role + " успешно добавлен");
    }

    @PostMapping("/add/task/custom-task")
    @RolesAllowed({"GOD", "TEACHER"})
    public ResponseEntity<String> addNewCustomTask(@RequestBody TaskDTO taskDTO) {

        // !!!здесь будет логика для добавления кастомного задания

        return ResponseEntity.status(HttpStatus.CREATED).body("Задание успешно добавлено");
    }

    @PostMapping("/add/user/student")
    @RolesAllowed({"GOD", "TEACHER"})
    public ResponseEntity<String> addNewStudent(@RequestBody UserDTO userDTO) {
        return addUserLogic(userDTO, Roles.STUDENT);
    }

    @PostMapping("/add/user/teacher")
    @RolesAllowed({"GOD", "MANAGER"})
    public ResponseEntity<String> addNewTeacher(@RequestBody UserDTO userDTO) {
        return addUserLogic(userDTO, Roles.TEACHER);
    }

    @PostMapping("/add/user/manager")
    @RolesAllowed("GOD")
    public ResponseEntity<String> addNewManager(@RequestBody UserDTO userDTO) {
        return addUserLogic(userDTO, Roles.MANAGER);
    }

    @PostMapping("/add/user/god")
    @RolesAllowed("GOD")
    public ResponseEntity<String> addNewGod(@RequestBody UserDTO userDTO) {
        return addUserLogic(userDTO, Roles.GOD);
    }
}
