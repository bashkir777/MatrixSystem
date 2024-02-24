package com.example.matrixsystem.beans;

import com.example.matrixsystem.dto.AddCustomTaskDTO;
import com.example.matrixsystem.security.beans.UserInformation;
import com.example.matrixsystem.spring_data.entities.*;
import com.example.matrixsystem.spring_data.entities.enums.UserTaskRelationTypes;
import com.example.matrixsystem.spring_data.exceptions.*;
import com.example.matrixsystem.spring_data.interfaces.CustomTaskRepository;
import com.example.matrixsystem.spring_data.interfaces.HomeworkCustomTaskRepository;
import com.example.matrixsystem.spring_data.interfaces.HomeworkRepository;
import com.example.matrixsystem.spring_data.interfaces.UserCustomTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.util.ArrayList;
import java.util.List;

@Service
@SessionScope
public class CustomDatabaseManager {
    private final CustomTaskRepository customTaskRepository;
    private final HomeworkRepository homeworkRepository;
    private final HomeworkCustomTaskRepository homeworkCustomTaskRepository;
    private final UserCustomTaskRepository userCustomTaskRepository;
    private final UserInformation userInformation;
    @Autowired
    public CustomDatabaseManager(CustomTaskRepository customTaskRepository, HomeworkRepository homeworkRepository
            ,HomeworkCustomTaskRepository homeworkCustomTaskRepository, UserCustomTaskRepository userCustomTaskRepository
            , UserInformation userInformation) {
        this.customTaskRepository =customTaskRepository;
        this.homeworkRepository = homeworkRepository;
        this.homeworkCustomTaskRepository = homeworkCustomTaskRepository;
        this.userCustomTaskRepository = userCustomTaskRepository;
        this.userInformation = userInformation;
    }

    public List<CustomTask> getAllCustomTasksOfHomework(Homework homework){
        List<CustomTask> toReturn = new ArrayList<>();
        List<HomeworkCustomTask> homeworkCustomTaskList
                = homeworkCustomTaskRepository.getHomeworkCustomTaskByHomework(homework);
        for(HomeworkCustomTask homeworkCustomTask: homeworkCustomTaskList){
            toReturn.add(homeworkCustomTask.getCustomTask());
        }
        return toReturn;
    }

    public List<UserCustomTask> getUserCustomTaskListOfCurrentUser() throws NoSuchUserInDB {
        return userCustomTaskRepository.getUserCustomTaskByUserReference(
                userInformation.getUser()
        );
    }

    public Homework getHomeworkById(Integer id) throws NoSuchHomeworkException{
        try{
            return homeworkRepository.getReferenceById(id);
        }catch (Exception e){
            throw new NoSuchHomeworkException();
        }
    }
    public CustomTask getCustomTaskById(Integer id) throws NoSuchCustomTaskException{
        try{
            return customTaskRepository.getReferenceById(id);
        }catch (Exception e){
            throw new NoSuchCustomTaskException();
        }
    }

    public void addUserCustomTaskRelation(CustomTask customTask, UserTaskRelationTypes relation) throws NoSuchUserInDB {

        userCustomTaskRepository.save(UserCustomTask.builder()
                .userReference(userInformation.getUser())
                .relationType(relation).taskReference(customTask).build());

    }

    public void deleteUserCustomTask(UserCustomTask userCustomTask){
        userCustomTaskRepository.delete(userCustomTask);
    }
    public void addCustomTask(CustomTask task) throws ErrorCreatingCustomTask {
        try {
            customTaskRepository.save(task);
        } catch (Exception e) {
            throw new ErrorCreatingCustomTask();
        }
    }
    public void addHomeworkCustomTask(HomeworkCustomTask homeworkCustomTask) throws ErrorCreatingHomeworkCustomTask {
        try {
            homeworkCustomTaskRepository.save(homeworkCustomTask);
        } catch (Exception e) {
            throw new ErrorCreatingHomeworkCustomTask();
        }
    }
    public void addHomework(Homework homework) throws ErrorCreatingHomework{
        try {
            homeworkRepository.save(homework);
        } catch (Exception e) {
            throw new ErrorCreatingHomework();
        }
    }
    public UserTaskRelationTypes getRelationBetweenCurrentUserAndCustomTask(CustomTask customTask) {
        List<UserCustomTask> list;
        try{
             list = userCustomTaskRepository
                    .getUserCustomTaskByUserReference(userInformation.getUser());
        }catch (NoSuchUserInDB e){
            return UserTaskRelationTypes.NONE;
        }

        UserTaskRelationTypes answer = UserTaskRelationTypes.NONE;
        for(UserCustomTask userCustomTask : list){
            if(userCustomTask.getTaskReference().equals(customTask)){
                answer = userCustomTask.getRelationType();
            }
        }
        return answer;
    }
}
