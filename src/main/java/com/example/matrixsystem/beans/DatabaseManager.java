package com.example.matrixsystem.beans;

import com.example.matrixsystem.spring_data.entities.Module;
import com.example.matrixsystem.spring_data.entities.ModuleTask;
import com.example.matrixsystem.spring_data.entities.Task;
import com.example.matrixsystem.spring_data.interfaces.ModuleRepository;
import com.example.matrixsystem.spring_data.interfaces.ModuleTaskRepository;
import com.example.matrixsystem.spring_data.interfaces.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.util.ArrayList;

@Component
@SessionScope
public class DatabaseManager {
    private final TaskRepository taskRepository;
    private final ModuleRepository moduleRepository;
    private final ModuleTaskRepository moduleTaskRepository;

    private ArrayList<Task> allTasks = new ArrayList<>();
    private ArrayList<Module> allModules = new ArrayList<>();
    private ArrayList<ModuleTask> allModuleTasks = new ArrayList<>();

    @Autowired
    public DatabaseManager(TaskRepository taskRepository, ModuleRepository moduleRepository
            , ModuleTaskRepository moduleTaskRepository){
        this.taskRepository = taskRepository;
        this.moduleRepository = moduleRepository;
        this.moduleTaskRepository = moduleTaskRepository;

        allTasks.addAll(taskRepository.findAll());
        allModules.addAll(moduleRepository.findAll());
        allModuleTasks.addAll(moduleTaskRepository.findAll());

        System.out.println(allTasks.size());
    }

    public ArrayList<Task> getAllTasks() {
        return allTasks;
    }

    public ArrayList<Module> getAllModules() {
        return allModules;
    }

    public ArrayList<ModuleTask> getAllModuleTasks() {
        return allModuleTasks;
    }
}
