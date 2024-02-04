package com.example.matrixsystem.controllers;

import com.example.matrixsystem.beans.DatabaseManager;
import com.example.matrixsystem.dto.TaskDTO;
import com.example.matrixsystem.dto.UserDTO;
import com.example.matrixsystem.exceptions.NoSuchModuleInDB;
import com.example.matrixsystem.exceptions.NoSuchTaskInDB;
import com.example.matrixsystem.security.beans.UserInformation;
import com.example.matrixsystem.spring_data.entities.Module;
import com.example.matrixsystem.spring_data.entities.Task;
import com.example.matrixsystem.spring_data.entities.Users;
import com.example.matrixsystem.spring_data.entities.enums.Roles;
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
    private final UserInformation userInfo;
    @Autowired
    public DatabaseController(DatabaseManager manager, UserInformation userInfo){
        this.manager = manager;
        this.userInfo = userInfo;
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
    @PostMapping("/add/task/common-pull")
    public ResponseEntity<String> addNewTaskToTheCommonPull(@RequestBody TaskDTO taskDTO){

        if(userInfo.getUserRole() != Roles.GOD){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("У вас нет прав на добавление заданий в общий пул");
        }
        Module module;
        try{
            module = manager.getModuleById(taskDTO.getModule());
        }catch (NoSuchModuleInDB noSuchModuleInDB){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(noSuchModuleInDB.getMessage());
        }

        Task task = Task.builder().task(taskDTO.getTask())
                .img(taskDTO.getImg()).answer(taskDTO.getAnswer())
                .module(module).build();

        if(manager.addNewTask(task)) return ResponseEntity.status(HttpStatus.CREATED).body("Задание успешно добавлено");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Задание не подходит под ограничения базы данных");
    }
    private ResponseEntity<String> addNewUserLogic(UserDTO userDTO, Roles role){
        Users user = Users.builder().login(userDTO.getLogin())
                .role(role).password(userDTO.getPassword()).build();

        if(manager.addNewUser(user)) return ResponseEntity.status(HttpStatus.CREATED).body(role + " успешно добавлен");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Пользователь с таким логином уже есть в базе данных");
    }
    @PostMapping("/add/task/custom-task")
    public ResponseEntity<String> addNewCustomTask(@RequestBody TaskDTO taskDTO){
        if(userInfo.getUserRole() == Roles.STUDENT && userInfo.getUserRole() == Roles.MANAGER){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("У вас нет прав на добавление заданий");
        }
        //здесь будет логика для добавления кастомного задания
        return ResponseEntity.status(HttpStatus.CREATED).body("Задание успешно добавлено");
    }

    @PostMapping("/add/user/student")
    public ResponseEntity<String> addNewStudent(@RequestBody UserDTO userDTO){
        Roles role = userInfo.getUserRole();
        if(role != Roles.TEACHER && role != Roles.GOD){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("У вас нет прав на добавление учеников");
        }
        return addNewUserLogic(userDTO, Roles.STUDENT);
    }

    @PostMapping("/add/user/teacher")
    public ResponseEntity<String> addNewTeacher(@RequestBody UserDTO userDTO){
        Roles role = userInfo.getUserRole();
        if(role != Roles.GOD && role != Roles.MANAGER){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("У вас нет прав на добавление учителей");
        }
        return addNewUserLogic(userDTO, Roles.TEACHER);
    }

    @PostMapping("/add/user/manager")
    public ResponseEntity<String> addNewManager(@RequestBody UserDTO userDTO){
        Roles role = userInfo.getUserRole();
        if(role != Roles.GOD){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("У вас нет прав на добавление менеджеров");
        }
        return addNewUserLogic(userDTO, Roles.MANAGER);
    }

    @PostMapping("/add/user/god")
    public ResponseEntity<String> addNewGod(@RequestBody UserDTO userDTO){
        Roles role = userInfo.getUserRole();
        if(role != Roles.GOD){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("У вас нет прав на добавление God");
        }
        return addNewUserLogic(userDTO, Roles.GOD);
    }
}
