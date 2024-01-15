package com.example.matrixsystem.controllers;

import com.example.matrixsystem.beans.DatabaseManager;
import com.example.matrixsystem.spring_data.entities.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;


@RestController
@RequestMapping("/api/v1")
public class DatabaseController {
    private final DatabaseManager manager;
    @Autowired
    public DatabaseController(DatabaseManager manager){
        this.manager = manager;
    }


    @GetMapping("/module/{num}")
    public ArrayList<Task> allTasks(@PathVariable Integer num){
        return manager.getAllModuleTasks(num);
    }
}
