package com.example.matrixsystem.controllers;

import com.example.matrixsystem.beans.DatabaseManager;
import com.example.matrixsystem.dto.TaskForSubmittingDTO;
import com.example.matrixsystem.security.beans.UserInformation;
import com.example.matrixsystem.spring_data.entities.Task;
import com.example.matrixsystem.spring_data.entities.UserTask;
import com.example.matrixsystem.spring_data.entities.Users;
import com.example.matrixsystem.spring_data.entities.enums.UserTaskRelationTypes;
import com.example.matrixsystem.spring_data.exceptions.*;
import com.example.matrixsystem.spring_data.annotations.HandleDataActionExceptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// этот контроллер содержит различные методы для поддержания корректной работы user интерфейса,
// который не связан с менеджментом

@RestController
@RequestMapping("/api/v1/client")
public class ClientInterfaceController {
    private final DatabaseManager manager;
    private final UserInformation userInformation;
    @Autowired
    public ClientInterfaceController(DatabaseManager manager, UserInformation userInformation) {
        this.manager = manager;
        this.userInformation = userInformation;
    }

    @GetMapping("/module/{num}")
    public List<Integer> allTasksIds(@PathVariable Integer num) {
        return manager.getAllModuleTasksIds(num);
    }

    @GetMapping("/module/{moduleNum}/task/{taskNum}")
    public ResponseEntity<Object> getTaskId(@PathVariable Integer moduleNum, @PathVariable Integer taskNum) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(manager.getAllModuleTasks(moduleNum).get(taskNum - 1).getId());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Модуль с таким id не найден " +
                    "/ задания с таким номером не существует");
        }
    }

    @GetMapping("/task/{taskId}")
    @HandleDataActionExceptions
    public ResponseEntity<Object> getTaskById(@PathVariable Integer taskId) throws NoSuchTaskInDB{
        return ResponseEntity.status(HttpStatus.OK).body(manager.getTaskById(taskId));
    }
    @GetMapping("module/{id}/tasks/statuses")
    @HandleDataActionExceptions
    public Map<Integer, String> getModuleTasksStatuses(@PathVariable Integer id)
            throws NoSuchUserInDB, NoSuchUserTaskRelation, NoSuchModuleInDB, NoSuchTaskInDB{

        List<Task> taskList = manager.getTasksByModule(manager.getModuleById(id));

        List<UserTask> userTaskList = manager.getUserTasksByUserReference(userInformation.getUser());
        HashMap<Integer, String> toReturn = new HashMap<>();

        for(UserTask userTask: userTaskList){
            toReturn.put(userTask.getTaskReference().getId(), userTask.getRelationType().toString());
        }
        for(Task task: taskList){
            Integer taskId = task.getId();
            if(!toReturn.containsKey(taskId)){
                toReturn.put(taskId, UserTaskRelationTypes.NONE.toString());
            }
        }
        return toReturn;
    }
    @GetMapping("task/{id}/status")
    @HandleDataActionExceptions
    public ResponseEntity<String> getTasksStatusById(@PathVariable Integer id)
            throws NoSuchUserInDB, NoSuchUserTaskRelation{
        List<UserTask> userTaskList = manager.getUserTasksByUserReference(userInformation.getUser());
        for(UserTask userTask: userTaskList){
            if(userTask.getTaskReference().getId().equals(id)){
                return ResponseEntity.status(HttpStatus.OK).body(userTask.getRelationType().name());
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(UserTaskRelationTypes.NONE.name());
    }

    @PostMapping("submit/task")
    @HandleDataActionExceptions
    public ResponseEntity<String> submitTask(@RequestBody TaskForSubmittingDTO dto) throws NoSuchTaskInDB, NoSuchUserInDB, ErrorCreatingUserTaskRecord {
        Task taskInDB = manager.getTaskById(dto.getId());
        Users user = userInformation.getUser();
        UserTaskRelationTypes result;
        if(taskInDB.getAnswer().equals(dto.getAnswer())){
            result = UserTaskRelationTypes.DONE;
            manager.addUserTaskRelation(user, taskInDB, result);
        }else{
            result = UserTaskRelationTypes.TRIED;
            manager.addUserTaskRelation(user, taskInDB, result);
        }
        return ResponseEntity.status(HttpStatus.OK).body(result.name());
    }


}
