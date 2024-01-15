package com.example.matrixsystem.beans;

import com.example.matrixsystem.spring_data.entities.Task;
import com.example.matrixsystem.spring_data.interfaces.ModuleRepository;
import com.example.matrixsystem.spring_data.interfaces.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.util.ArrayList;
import java.util.HashMap;

@Service
@SessionScope
public class DatabaseManager {

    private final HashMap<Integer, ArrayList<Task>> moduleTaskMap = new HashMap<>();

    @Autowired
    public DatabaseManager(TaskRepository taskRepository, ModuleRepository moduleRepository) {
        //moduleTaskMap initialization
        for (int i = 0; i < moduleRepository.findAll().size(); i++){
            moduleTaskMap.put(i, new ArrayList<>());
        }
        for (Task task : taskRepository.findAll()) {
            int moduleId = task.getModuleId();
            moduleTaskMap.get(moduleId-1).add(task);
        }
    }
    public ArrayList<Task> getAllModuleTasks(int moduleId){
        return moduleTaskMap.get(moduleId);
    }

    public HashMap<Integer, Integer> getModuleTaskCounterMap(){
        HashMap<Integer, Integer> toReturn = new HashMap<>();
        for (int i = 0; i < moduleTaskMap.size(); i++){
            toReturn.put(i, moduleTaskMap.get(i).size());
        }
        return toReturn;
    }
}
