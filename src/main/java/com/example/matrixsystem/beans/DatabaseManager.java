package com.example.matrixsystem.beans;

import com.example.matrixsystem.dto.ModuleDTO;
import com.example.matrixsystem.dto.TaskForOptionsDTO;
import com.example.matrixsystem.security.beans.UserInformation;
import com.example.matrixsystem.spring_data.entities.*;
import com.example.matrixsystem.spring_data.entities.Module;
import com.example.matrixsystem.spring_data.entities.enums.Roles;
import com.example.matrixsystem.spring_data.entities.enums.UserTaskRelationTypes;
import com.example.matrixsystem.spring_data.exceptions.*;
import com.example.matrixsystem.spring_data.interfaces.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.util.*;

@Service
@SessionScope
public class DatabaseManager {
    private final TaskRepository taskRepository;
    private final ModuleRepository moduleRepository;
    private final UserRepository userRepository;
    private final UserTaskRepository userTaskRepository;
    private final UserInformation userInformation;
    private final SectionRepository sectionRepository;

    @Autowired
    public DatabaseManager(TaskRepository taskRepository, ModuleRepository moduleRepository
            , UserRepository userRepository, UserTaskRepository userTaskRepository
            , UserInformation userInformation, SectionRepository sectionRepository) {
        this.taskRepository = taskRepository;
        this.moduleRepository = moduleRepository;
        this.userRepository = userRepository;
        this.userTaskRepository = userTaskRepository;
        this.userInformation = userInformation;
        this.sectionRepository = sectionRepository;
    }
    public List<Integer> getAllModuleTasksIds(Module module){
        return taskRepository.getTasksByModule(module).stream().map(Task::getId).toList();
    }

    public HashMap<Integer, Integer> getModuleTaskCounterMap(){
        HashMap<Integer, Integer> toReturn = new HashMap<>();

        List<Module> modules = moduleRepository.findAll();

        for (Module module: modules){
            toReturn.put(module.getId(), taskRepository.getTasksByModule(module).size());
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
    public Integer getModuleCapacity(Module module){
        return  taskRepository.getTasksByModule(module).size();
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

    public List<UserTask> getUserTasksByUserReference(Users user) throws NoSuchUserTaskRelation{
        try{
            return userTaskRepository.getUserTasksByUserReference(user);
        }catch (Exception e){
            throw new NoSuchUserTaskRelation();
        }
    }

    public List<Task> getTasksByModule(Module module) throws NoSuchTaskInDB{
        try{
            return taskRepository.getTasksByModule(module);
        }catch (Exception e){
            throw new NoSuchTaskInDB();
        }
    }

    public void deleteUserTask(UserTask userTask) throws ErrorDeletingUserTaskRecord{
        userTaskRepository.delete(userTask);
    }

    public UserTask getCurrentUserTaskByTask(Task task) throws NoSuchUserInDB, NoSuchUserTaskRelation {
        List<UserTask> userTaskList = getUserTasksByUserReference(userInformation.getUser());

        for(UserTask userTask: userTaskList){
            Integer taskIdOfPair = userTask.getTaskReference().getId();

            if(taskIdOfPair.equals(task.getId())){
                return userTask;
            }
        }
        throw new NoSuchUserTaskRelation();
    }

    // возвращает статус связи текущего пользователя и переданного задания
    public UserTaskRelationTypes getUserTaskRelationByTask(Task task) throws NoSuchUserInDB, NoSuchUserTaskRelation {
        List<UserTask> userTaskList = getUserTasksByUserReference(userInformation.getUser());

        for(UserTask userTask: userTaskList){
            Integer taskIdOfPair = userTask.getTaskReference().getId();

            if(taskIdOfPair.equals(task.getId())){
                return userTask.getRelationType();
            }
        }
        return UserTaskRelationTypes.NONE;
    }
    // удаляет связь user-task текущего пользователя с переданным в параметре заданием
    public void deleteUserTaskRelationOfCurrentUserByTask(Task task) throws NoSuchUserTaskRelation
            , NoSuchUserInDB, ErrorDeletingUserTaskRecord {
        this.deleteUserTask(getCurrentUserTaskByTask(task));
    }

    public Integer counterOfDoneCurrentUserTaskRelationsDone(Module module) throws NoSuchUserInDB {
        List<Task> tasks = taskRepository.getTasksByModule(module);

        int counter = 0;
        for(Task task: tasks){
            try{
                if(getUserTaskRelationByTask(task).equals(UserTaskRelationTypes.DONE)){
                    counter ++;
                }
            }catch (NoSuchUserTaskRelation ignored){
            }
        }
        return counter;
    }

    public HashMap<Integer, Integer> getDoneMap() throws NoSuchUserInDB {
        List<Module> listModules = moduleRepository.findAll();
        HashMap<Integer, Integer> doneMap = new HashMap<>();
        for(Module module: listModules){
            doneMap.put(module.getId(), counterOfDoneCurrentUserTaskRelationsDone(module));
        }
        return doneMap;
    }

    public HashMap<Integer, String> getModuleNameMap(){
        HashMap<Integer, String> moduleNameMap = new HashMap<>();
        moduleRepository.findAll().forEach((module) -> {
            moduleNameMap.put(module.getId(), module.getName());
        });
        return moduleNameMap;
    }

    public List<Module> getAllModules(){
        return moduleRepository.findAll();
    }

    public List<TaskForOptionsDTO> generateOption(){
        List<TaskForOptionsDTO> toReturn = new ArrayList<>();
        for(Module module: moduleRepository.findAll()){
            List<Task> list = taskRepository.getTasksByModule(module);
            Random random = new Random();
            int randomIndex = random.nextInt(list.size());
            Task randomTask = list.get(randomIndex);
            Module randomTaskModule = randomTask.getModule();
            TaskForOptionsDTO randomTaskDTO = TaskForOptionsDTO.builder()
                    .module(ModuleDTO.builder().id(randomTaskModule.getId())
                            .verifiable(randomTaskModule.getVerifiable()).maxPoints(module.getMaxPoints())
                            .build()).task(randomTask.getTask())
                    .answer(randomTask.getAnswer()).img(randomTask.getImg()).build();

            toReturn.add(randomTaskDTO);
        }
        return toReturn;
    }

    public void addSection(Section section) throws ErrorCreatingSection{
        try{
            sectionRepository.save(section);
        }catch (Exception e){
            throw new ErrorCreatingSection();
        }
    }
    public void deleteSection(Section section) throws ErrorDeletingSection{
        try{
            sectionRepository.delete(section);
        }catch (Exception e){
            throw new ErrorDeletingSection();
        }
    }

    public Section getSectionById(Integer id) throws NoSuchSectionException{
        try{
            return sectionRepository.getSectionsById(id);
        }catch (Exception e){
            throw new NoSuchSectionException();
        }
    }
}
