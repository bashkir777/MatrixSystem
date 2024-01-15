package com.example.matrixsystem.beans;

import com.example.matrixsystem.spring_data.entities.Task;
import com.example.matrixsystem.spring_data.interfaces.ModuleRepository;
import com.example.matrixsystem.spring_data.interfaces.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.util.ArrayList;
import java.util.HashMap;

@Component
@SessionScope
public class DatabaseManager {

    private final HashMap<Integer, ArrayList<Task>> moduleTaskMap = new HashMap<>();

    @Autowired
    public DatabaseManager(TaskRepository taskRepository, ModuleRepository moduleRepository) {
        //moduleTaskMap initialization
        for (Task task : taskRepository.findAll()) {
            int moduleId = task.getModuleId();
            ArrayList<Task> list = moduleTaskMap.computeIfAbsent(moduleId, k -> new ArrayList<>());
            list.add(task);
        }
    }
    public ArrayList<Task> getAllModuleTasks(int moduleId){
        return moduleTaskMap.get(moduleId);
    }
}
