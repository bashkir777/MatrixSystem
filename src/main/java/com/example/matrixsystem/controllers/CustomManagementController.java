package com.example.matrixsystem.controllers;

import com.example.matrixsystem.beans.CustomDatabaseManager;
import com.example.matrixsystem.dto.*;
import com.example.matrixsystem.security.annotations.RolesAllowed;
import com.example.matrixsystem.security.beans.UserInformation;
import com.example.matrixsystem.spring_data.annotations.HandleDataActionExceptions;
import com.example.matrixsystem.spring_data.entities.CustomTask;
import com.example.matrixsystem.spring_data.entities.Module;
import com.example.matrixsystem.spring_data.entities.Task;
import com.example.matrixsystem.spring_data.entities.UserCustomTask;
import com.example.matrixsystem.spring_data.entities.enums.UserTaskRelationTypes;
import com.example.matrixsystem.spring_data.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

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
    public ResponseEntity<String> getCustomTaskStatus(@PathVariable Integer id) throws NoSuchCustomTaskException
            , NoSuchUserInDB {
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
    @GetMapping("custom-task/{id}")
    @HandleDataActionExceptions
    public ResponseEntity<CustomTaskDTO> getCustomTask(@PathVariable Integer id) throws NoSuchCustomTaskException,
            NoSuchUserInDB {
        CustomTask task = manager.getCustomTaskById(id);

        manager.addUserCustomTaskRelation(task, UserTaskRelationTypes.FAILED);
        return ResponseEntity.status(HttpStatus.OK).body(CustomTaskDTO.builder()
                .answer(task.getAnswer()).solution(task.getSolution()).task(task.getTask())
                .verifiable(task.getVerifiable()).img(task.getImg()).build());
    }

    @PostMapping("/add/task/custom-pull")
    @RolesAllowed({"GOD", "TEACHER"})
    @HandleDataActionExceptions
    public ResponseEntity<Integer> addNewTaskToTheCustomPull(@RequestParam("task") String task,
                                                            @RequestParam(value = "answer", required = false) String answer,
                                                            @RequestParam("verifiable") Boolean verifiable,
                                                            @RequestParam(value = "solution", required = false) String solution,
                                                            @RequestPart(value = "image", required = false) MultipartFile image)
            throws IOException, ErrorCreatingCustomTask {
        CustomTask.CustomTaskBuilder builder = CustomTask.builder();
        if(verifiable != null){
            builder.verifiable(verifiable);
        }
        if (image != null) {
            String fileName = UUID.randomUUID() + "." + Objects.requireNonNull(image.getContentType()).split("/")[1];
            String uploadDir = "db/images/";
            Path filePath = Paths.get(uploadDir, fileName);
            Files.write(filePath, image.getBytes());
            builder.img(filePath.toString());
        }
        if(solution != null){
            builder.solution(solution);
        }

        CustomTask taskObj = builder.task(task)
                .answer(answer).build();


        manager.addCustomTask(taskObj);

        return ResponseEntity.status(HttpStatus.CREATED).body(taskObj.getId());
    }


}
