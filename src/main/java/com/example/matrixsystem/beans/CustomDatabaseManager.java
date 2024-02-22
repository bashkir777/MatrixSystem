package com.example.matrixsystem.beans;

import com.example.matrixsystem.spring_data.entities.CustomTask;
import com.example.matrixsystem.spring_data.entities.Homework;
import com.example.matrixsystem.spring_data.entities.HomeworkCustomTask;
import com.example.matrixsystem.spring_data.exceptions.NoSuchHomeworkException;
import com.example.matrixsystem.spring_data.interfaces.CustomTaskRepository;
import com.example.matrixsystem.spring_data.interfaces.HomeworkCustomTaskRepository;
import com.example.matrixsystem.spring_data.interfaces.HomeworkRepository;
import com.example.matrixsystem.spring_data.interfaces.UserCustomTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
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
    @Autowired
    public CustomDatabaseManager(CustomTaskRepository customTaskRepository, HomeworkRepository homeworkRepository
            ,HomeworkCustomTaskRepository homeworkCustomTaskRepository, UserCustomTaskRepository userCustomTaskRepository) {
        this.customTaskRepository =customTaskRepository;
        this.homeworkRepository = homeworkRepository;
        this.homeworkCustomTaskRepository = homeworkCustomTaskRepository;
        this.userCustomTaskRepository = userCustomTaskRepository;
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

    public Homework getHomeworkById(Integer id) throws NoSuchHomeworkException{
        try{
            return homeworkRepository.getReferenceById(id);
        }catch (Exception e){
            throw new NoSuchHomeworkException();
        }
    }

}
