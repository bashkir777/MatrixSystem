package com.example.matrixsystem.beans;

import com.example.matrixsystem.exceptions.NoSuchTaskInDB;
import com.example.matrixsystem.spring_data.entities.Task;
import com.example.matrixsystem.spring_data.interfaces.ModuleRepository;
import com.example.matrixsystem.spring_data.interfaces.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
@SessionScope
public class DatabaseManager {

    private final HashMap<Integer, ArrayList<Task>> moduleTaskMap = new HashMap<>();

    private final TaskRepository taskRepository;
    @Autowired
    public DatabaseManager(TaskRepository taskRepository, ModuleRepository moduleRepository) {
        this.taskRepository = taskRepository;
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
    public List<Integer> getAllModuleTasksIds(int moduleId){
        return moduleTaskMap.get(moduleId-1).stream().map(Task::getId).toList();
    }

    public HashMap<Integer, Integer> getModuleTaskCounterMap(){
        HashMap<Integer, Integer> toReturn = new HashMap<>();
        for (int i = 0; i < moduleTaskMap.size(); i++){
            toReturn.put(i, moduleTaskMap.get(i).size());
        }
        return toReturn;
    }

    public Task getTaskById(Integer id) throws NoSuchTaskInDB{
        Optional<Task> optional = taskRepository.findById(id);
        if (optional.isPresent()){
            return optional.get();
        }else{
            throw new NoSuchTaskInDB();
        }
    }
}
