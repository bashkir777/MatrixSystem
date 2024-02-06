package com.example.matrixsystem.beans;

import com.example.matrixsystem.spring_data.entities.Module;
import com.example.matrixsystem.spring_data.entities.Task;
import com.example.matrixsystem.spring_data.entities.UserTask;
import com.example.matrixsystem.spring_data.entities.Users;
import com.example.matrixsystem.spring_data.entities.enums.Roles;
import com.example.matrixsystem.spring_data.entities.enums.UserTaskRelationTypes;
import com.example.matrixsystem.spring_data.exceptions.*;
import com.example.matrixsystem.spring_data.interfaces.ModuleRepository;
import com.example.matrixsystem.spring_data.interfaces.TaskRepository;
import com.example.matrixsystem.spring_data.interfaces.UserRepository;
import com.example.matrixsystem.spring_data.interfaces.UserTaskRepository;
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
    private final UserTaskRepository userTaskRepository;
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
    public DatabaseManager(TaskRepository taskRepository, ModuleRepository moduleRepository
            , UserRepository userRepository, UserTaskRepository userTaskRepository) {
        this.taskRepository = taskRepository;
        this.moduleRepository = moduleRepository;
        this.userRepository = userRepository;
        this.userTaskRepository = userTaskRepository;
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

    public Task getTaskById(Integer id) throws NoSuchTaskInDB {
        Optional<Task> optional = taskRepository.findById(id);
        if (optional.isPresent()){
            return optional.get();
        }else{
            throw new NoSuchTaskInDB();
        }
    }
    public void addTask(Task task) throws ErrorCreatingTaskRecord {
        try {
            taskRepository.save(task);
        }catch (Exception e){
            throw new ErrorCreatingTaskRecord();
        }
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
    public void addUser(Users user) throws ErrorCreatingUserRecord {
        try {
            userRepository.save(user);
        }catch (Exception e){
            throw new ErrorCreatingUserRecord();
        }
    }

    public void addUserTaskRelation(Users user, Task task, UserTaskRelationTypes relationType) throws ErrorCreatingUserTaskRecord {
        try {
            UserTask userTask = UserTask.builder().taskReference(task)
                    .userReference(user).relationType(relationType).build();
            this.userTaskRepository.save(userTask);
        }catch (Exception e){
            throw new ErrorCreatingUserTaskRecord();
        }
    }
}
