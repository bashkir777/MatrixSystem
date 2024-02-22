package com.example.matrixsystem.beans;

import com.example.matrixsystem.security.beans.UserInformation;
import com.example.matrixsystem.spring_data.entities.*;
import com.example.matrixsystem.spring_data.exceptions.NoSuchHomeworkException;
import com.example.matrixsystem.spring_data.exceptions.NoSuchUserInDB;
import com.example.matrixsystem.spring_data.interfaces.CustomTaskRepository;
import com.example.matrixsystem.spring_data.interfaces.HomeworkCustomTaskRepository;
import com.example.matrixsystem.spring_data.interfaces.HomeworkRepository;
import com.example.matrixsystem.spring_data.interfaces.UserCustomTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

}
