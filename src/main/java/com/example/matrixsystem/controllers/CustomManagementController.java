package com.example.matrixsystem.controllers;

import com.example.matrixsystem.beans.CustomDatabaseManager;
import com.example.matrixsystem.dto.*;
import com.example.matrixsystem.security.annotations.RolesAllowed;
import com.example.matrixsystem.security.beans.UserInformation;
import com.example.matrixsystem.spring_data.annotations.HandleDataActionExceptions;
import com.example.matrixsystem.spring_data.entities.CustomTask;
import com.example.matrixsystem.spring_data.entities.UserCustomTask;
import com.example.matrixsystem.spring_data.entities.enums.UserTaskRelationTypes;
import com.example.matrixsystem.spring_data.exceptions.NoSuchCustomTaskException;
import com.example.matrixsystem.spring_data.exceptions.NoSuchHomeworkException;
import com.example.matrixsystem.spring_data.exceptions.NoSuchUserInDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/management")
public class CustomManagementController {
    private final CustomDatabaseManager manager;
    private final UserInformation userInformation;

    @Autowired
    public CustomManagementController(CustomDatabaseManager manager, UserInformation userInformation) {
        this.manager = manager;
        this.userInformation = userInformation;
    }
    @PostMapping("/add/task/custom-task")
    @RolesAllowed({"GOD", "TEACHER"})
    public ResponseEntity<String> addNewCustomTask(@RequestBody TaskForAddingDTO taskForAddingDTO) {

        // !!!здесь будет логика для добавления кастомного задания

        return ResponseEntity.status(HttpStatus.CREATED).body("Задание успешно добавлено");
    }

    @GetMapping("/homework/info/{id}")
    public HomeworkInfoDTO getHomeworkInfo(@PathVariable Integer id){
        HomeworkInfoDTO.HomeworkInfoDTOBuilder builder = HomeworkInfoDTO.builder();
        try{
            builder.task_counter(manager.getHomeworkById(id).getHomeworkCustomTasks().size());
        }catch (Exception e){
            builder.task_counter(0);
        }
        return builder.build();
    }

    @GetMapping("/homework/{id}")
    public List<CustomTaskDTO> getHomework(@PathVariable Integer id) throws NoSuchHomeworkException, NoSuchUserInDB {
        List<CustomTaskDTO> customTaskDTOS = new ArrayList<>();
        List<CustomTask> customTaskList = manager.getAllCustomTasksOfHomework(manager.getHomeworkById(id));
        customTaskList.forEach(t->
            {
                CustomTaskDTO.CustomTaskDTOBuilder builder = CustomTaskDTO.builder().task(t.getTask())
                        .solution(t.getSolution()).answer(t.getAnswer())
                        .img(t.getImg()).id(t.getId()).verifiable(t.getVerifiable());
                customTaskDTOS.add(builder.build());
            }
        );
        return customTaskDTOS;
    }
    @PostMapping("/submit/custom-task")
    @HandleDataActionExceptions
    public ResponseEntity<String> submitTask(@RequestBody SubmitCustomTaskDTO submitCustomTaskDTO)
            throws NoSuchCustomTaskException, NoSuchUserInDB {
        UserTaskRelationTypes answer;

        for(UserCustomTask userCustomTask : manager.getUserCustomTaskListOfCurrentUser()){
            if(userCustomTask.getTaskReference()
                    .equals(manager.getCustomTaskById(submitCustomTaskDTO.getId()))){
                if(userCustomTask.getRelationType().equals(UserTaskRelationTypes.DONE)){
                    return ResponseEntity.status(HttpStatus.OK).body("Вы уже сделали это задание");
                }else{
                    manager.deleteUserCustomTask(userCustomTask);
                }
            }
        }

        CustomTask customTask = manager.getCustomTaskById(submitCustomTaskDTO.getId());

        if(!customTask.getVerifiable()){
            manager.addUserCustomTaskRelation(customTask, UserTaskRelationTypes.DONE);
            answer = UserTaskRelationTypes.DONE;
        }

        if(customTask.getAnswer().equals(submitCustomTaskDTO.getAnswer())){
            manager.addUserCustomTaskRelation(customTask, UserTaskRelationTypes.DONE);
            answer = UserTaskRelationTypes.DONE;
        }else{
            manager.addUserCustomTaskRelation(customTask, UserTaskRelationTypes.TRIED);
            answer = UserTaskRelationTypes.TRIED;
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(answer.name());
    }
    @GetMapping("custom-task/status/{id}")
    @HandleDataActionExceptions
    public ResponseEntity<String> getCustomTaskStatus(@PathVariable Integer id) throws NoSuchCustomTaskException, NoSuchUserInDB {
        for(UserCustomTask userCustomTask : manager.getUserCustomTaskListOfCurrentUser()){
            if(userCustomTask.getTaskReference()
                    .equals(manager.getCustomTaskById(id))){

                return ResponseEntity.status(HttpStatus.OK).body(userCustomTask.getRelationType().name());

            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(UserTaskRelationTypes.NONE.name());
    }

    @GetMapping("custom-task/{id}/answer")
    @HandleDataActionExceptions
    public ResponseEntity<TaskAnswerDTO> getCustomTaskAnswer(@PathVariable Integer id) throws NoSuchCustomTaskException,
            NoSuchUserInDB {
        CustomTask task = manager.getCustomTaskById(id);

        for(UserCustomTask userCustomTask : manager.getUserCustomTaskListOfCurrentUser()){
            if(userCustomTask.getTaskReference()
                    .equals(manager.getCustomTaskById(id))){
                if(userCustomTask.getRelationType().equals(UserTaskRelationTypes.DONE)){
                    return ResponseEntity.status(HttpStatus.OK).body(TaskAnswerDTO.builder()
                            .answer(task.getAnswer()).solution(task.getSolution()).build());
                }else{
                    manager.deleteUserCustomTask(userCustomTask);
                }
            }
        }
        manager.addUserCustomTaskRelation(task, UserTaskRelationTypes.FAILED);
        return ResponseEntity.status(HttpStatus.OK).body(TaskAnswerDTO.builder()
                .answer(task.getAnswer()).solution(task.getSolution()).build());

    }
}
