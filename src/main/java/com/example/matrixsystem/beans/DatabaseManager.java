package com.example.matrixsystem.beans;

import com.example.matrixsystem.exceptions.NoSuchModuleInDB;
import com.example.matrixsystem.exceptions.NoSuchTaskInDB;
import com.example.matrixsystem.exceptions.NoSuchUserInDB;
import com.example.matrixsystem.spring_data.entities.Module;
import com.example.matrixsystem.spring_data.entities.Task;
import com.example.matrixsystem.spring_data.entities.Users;
import com.example.matrixsystem.spring_data.entities.enums.Roles;
import com.example.matrixsystem.spring_data.interfaces.ModuleRepository;
import com.example.matrixsystem.spring_data.interfaces.TaskRepository;
import com.example.matrixsystem.spring_data.interfaces.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
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
    private final ModuleRepository moduleRepository;
    private final UserRepository userRepository;
    //обновляем задания каждые 10 минут
    @Scheduled(fixedDelay = 600000)
    public void renewMap(){
        moduleTaskMap.clear();
        for (int i = 0; i < moduleRepository.findAll().size(); i++){
            moduleTaskMap.put(i, new ArrayList<>());
        }
        for (Task task : taskRepository.findAll()) {
            int moduleId = task.getModule().getId();
            moduleTaskMap.get(moduleId-1).add(task);
        }

    }

    @Autowired
    public DatabaseManager(TaskRepository taskRepository, ModuleRepository moduleRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.moduleRepository = moduleRepository;
        this.userRepository = userRepository;
        this.renewMap();
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
    public boolean addNewTask(Task task){
        try {
            taskRepository.save(task);
        }catch (Exception e){
            return false;
        }
        return true;
    }

    public Module getModuleById(Integer id) throws NoSuchModuleInDB {
        Optional<Module> optionalModule = moduleRepository.findById(id);
        if (optionalModule.isPresent()) return optionalModule.get();
        throw new NoSuchModuleInDB();
    }

    public Roles getUserRoleByLogin(String login){
        return userRepository.findByLogin(login).getRole();
    }

    public Users getUserByLogin(String login) throws NoSuchUserInDB{
        Users user = userRepository.findByLogin(login);
        if(user == null){
            throw new NoSuchUserInDB();
        }
        return user;
    }
    public boolean addNewUser(Users user){
        try {
            userRepository.save(user);
        }catch (Exception e){
            return false;
        }
        return true;
    }
}
