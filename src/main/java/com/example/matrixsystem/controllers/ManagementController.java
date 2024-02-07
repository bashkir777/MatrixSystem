package com.example.matrixsystem.controllers;

import com.example.matrixsystem.beans.DatabaseManager;
import com.example.matrixsystem.dto.TaskForAddingDTO;
import com.example.matrixsystem.dto.UserDTO;
import com.example.matrixsystem.spring_data.exceptions.ErrorCreatingTaskRecord;
import com.example.matrixsystem.spring_data.exceptions.ErrorCreatingUserRecord;
import com.example.matrixsystem.spring_data.exceptions.NoSuchModuleInDB;
import com.example.matrixsystem.security.annotations.RolesAllowed;
import com.example.matrixsystem.spring_data.annotations.HandleDataActionExceptions;
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
@RequestMapping("/api/v1/management")
public class ManagementController {
    private final DatabaseManager manager;

    @Autowired
    public ManagementController(DatabaseManager manager) {
        this.manager = manager;
    }

    @PostMapping("/add/task/common-pull")
    @RolesAllowed("GOD")
    @HandleDataActionExceptions
    public ResponseEntity<String> addNewTaskToTheCommonPull(@RequestBody TaskForAddingDTO taskForAddingDTO) throws NoSuchModuleInDB,
            ErrorCreatingTaskRecord{
        Module module;

        module = manager.getModuleById(taskForAddingDTO.getModule());

        Task task = Task.builder().task(taskForAddingDTO.getTask())
                .img(taskForAddingDTO.getImg()).answer(taskForAddingDTO.getAnswer())
                .module(module).build();

        manager.addTask(task);

        return ResponseEntity.status(HttpStatus.CREATED).body("Задание успешно добавлено");
    }

    private ResponseEntity<String> addUserLogic(UserDTO userDTO, Roles role) throws ErrorCreatingUserRecord{
        Users user = Users.builder().login(userDTO.getLogin())
                .role(role).password(userDTO.getPassword()).build();
        manager.addUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(role + " успешно добавлен");
    }

    @PostMapping("/add/task/custom-task")
    @RolesAllowed({"GOD", "TEACHER"})
    public ResponseEntity<String> addNewCustomTask(@RequestBody TaskForAddingDTO taskForAddingDTO) {

        // !!!здесь будет логика для добавления кастомного задания

        return ResponseEntity.status(HttpStatus.CREATED).body("Задание успешно добавлено");
    }

    @PostMapping("/add/user/student")
    @RolesAllowed({"GOD", "TEACHER"})
    @HandleDataActionExceptions
    public ResponseEntity<String> addNewStudent(@RequestBody UserDTO userDTO) throws ErrorCreatingUserRecord{
        return addUserLogic(userDTO, Roles.STUDENT);
    }

    @PostMapping("/add/user/teacher")
    @RolesAllowed({"GOD", "MANAGER"})
    @HandleDataActionExceptions
    public ResponseEntity<String> addNewTeacher(@RequestBody UserDTO userDTO) throws ErrorCreatingUserRecord{
        return addUserLogic(userDTO, Roles.TEACHER);
    }

    @PostMapping("/add/user/manager")
    @RolesAllowed("GOD")
    @HandleDataActionExceptions
    public ResponseEntity<String> addNewManager(@RequestBody UserDTO userDTO) throws ErrorCreatingUserRecord{
        return addUserLogic(userDTO, Roles.MANAGER);
    }

    @PostMapping("/add/user/god")
    @RolesAllowed("GOD")
    @HandleDataActionExceptions
    public ResponseEntity<String> addNewGod(@RequestBody UserDTO userDTO) throws ErrorCreatingUserRecord{
        return addUserLogic(userDTO, Roles.GOD);
    }
}
