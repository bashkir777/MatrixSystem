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

    @GetMapping("/module/{id}")
    @HandleDataActionExceptions
    public List<Integer> allTasksIds(@PathVariable Integer id) throws NoSuchModuleInDB {
        return manager.getAllModuleTasksIds(manager.getModuleById(id));
    }


    @GetMapping("/task/{taskId}")
    @HandleDataActionExceptions
    public ResponseEntity<Object> getTaskById(@PathVariable Integer taskId) throws NoSuchTaskInDB{
        return ResponseEntity.status(HttpStatus.OK).body(manager.getTaskById(taskId));
    }
    @GetMapping("/module/{id}/tasks/statuses")
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
    @GetMapping("/task/{id}/status")
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

    @PostMapping("/task/submit")
    @HandleDataActionExceptions
    public ResponseEntity<String> submitTask(@RequestBody TaskForSubmittingDTO dto) throws NoSuchTaskInDB
            , NoSuchUserInDB, ErrorCreatingUserTaskRecord, NoSuchUserTaskRelation, ErrorDeletingUserTaskRecord {
        Task taskInDB = manager.getTaskById(dto.getId());

        UserTaskRelationTypes relation = manager.getUserTaskRelationByTask(taskInDB);

        if(relation.equals(UserTaskRelationTypes.DONE)  || relation.equals(UserTaskRelationTypes.FAILED)){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Нельзя изменять задания со статусом DONE или FAILED");
        }else if(relation.equals(UserTaskRelationTypes.TRIED)){
            manager.deleteUserTaskRelationOfCurrentUserByTask(taskInDB);
        }

        Users user = userInformation.getUser();
        UserTaskRelationTypes result;
        // Сверяем ответ задания из базы данных и ответ из dto
        // Добавляем связь user-task в соответствии с результатом проверки
        if(taskInDB.getAnswer().equals(dto.getAnswer())){
            result = UserTaskRelationTypes.DONE;
            manager.addUserTaskRelation(user, taskInDB, result);
        }else{
            result = UserTaskRelationTypes.TRIED;
            manager.addUserTaskRelation(user, taskInDB, result);
        }
        return ResponseEntity.status(HttpStatus.OK).body(result.name());
    }

    @GetMapping("/task/{id}/answer")
    @HandleDataActionExceptions
    public ResponseEntity<String> getAnswer(@PathVariable Integer id) throws NoSuchTaskInDB, NoSuchUserInDB, ErrorCreatingUserTaskRecord, ErrorDeletingUserTaskRecord {
        Task task = manager.getTaskById(id);
        UserTask userTask;
        Users user = userInformation.getUser();
        // если статус none то выброситься ошибка NoSuchUserTaskRelation
        // отлавливаем ее и стави FAILED
        try{
            userTask = manager.getCurrentUserTaskByTask(task);
        }catch (NoSuchUserTaskRelation e){
            manager.addUserTaskRelation(user, task, UserTaskRelationTypes.FAILED);
            return ResponseEntity.status(HttpStatus.OK).body(task.getAnswer());
        }

        UserTaskRelationTypes relation = userTask.getRelationType();
        if(relation.equals(UserTaskRelationTypes.TRIED)){
            manager.deleteUserTask(userTask);
            manager.addUserTaskRelation(user, task, UserTaskRelationTypes.FAILED);
        }
        return ResponseEntity.status(HttpStatus.OK).body(task.getAnswer());
    }
    @GetMapping("/module/{id}/done")
    @HandleDataActionExceptions
    public ResponseEntity<String> getTasksDoneCounter(@PathVariable Integer id) throws NoSuchModuleInDB, NoSuchUserInDB {

        return ResponseEntity.status(HttpStatus.OK).body(
                Integer.toString(manager.counterOfDoneCurrentUserTaskRelationsDone(manager.getModuleById(id)))
        );
    }

    @GetMapping("/options/autogenerate")
    @HandleDataActionExceptions
    public List<Task> getRandomOption() {
        return manager.generateOption();
    }

}
