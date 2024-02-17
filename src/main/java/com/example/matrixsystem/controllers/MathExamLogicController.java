package com.example.matrixsystem.controllers;

import com.example.matrixsystem.beans.DatabaseManager;
import com.example.matrixsystem.dto.FeedbackForOptionSubmissionDTO;
import com.example.matrixsystem.dto.TaskForOptionSubmittingDTO;
import com.example.matrixsystem.spring_data.annotations.HandleDataActionExceptions;
import com.example.matrixsystem.spring_data.entities.Module;
import com.example.matrixsystem.spring_data.entities.Task;
import com.example.matrixsystem.spring_data.exceptions.NoSuchModuleInDB;
import com.example.matrixsystem.spring_data.exceptions.NoSuchTaskInDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// в этом контроллере содержится логика оценки экзамена по профильной математике
// скорее всего эта логика будет меняться от года к году

@RestController
@RequestMapping("/api/v1/math")
public class MathExamLogicController {
    private final DatabaseManager manager;
    @Autowired
    public MathExamLogicController(DatabaseManager manager){
        this.manager = manager;
    }

    private static final Map<Integer, Integer> translationScale = new HashMap<>();
    static {
        translationScale.put(0, 0);
        translationScale.put(1, 6);
        translationScale.put(2, 11);
        translationScale.put(3, 17);
        translationScale.put(4, 22);
        translationScale.put(5, 27);
        translationScale.put(6, 34);
        translationScale.put(7, 40);
        translationScale.put(8, 46);
        translationScale.put(9, 52);
        translationScale.put(10, 58);
        translationScale.put(11, 64);
        translationScale.put(12, 66);
        translationScale.put(13, 68);
        translationScale.put(14, 70);
        translationScale.put(15, 72);
        translationScale.put(16, 74);
        translationScale.put(17, 76);
        translationScale.put(18, 78);
        translationScale.put(19, 80);
        translationScale.put(20, 82);
        translationScale.put(21, 84);
        translationScale.put(22, 86);
        translationScale.put(23, 88);
        translationScale.put(24, 90);
        translationScale.put(25, 92);
        translationScale.put(26, 94);
        translationScale.put(27, 96);
        translationScale.put(28, 98);
        translationScale.put(29, 100);
        translationScale.put(30, 100);
        translationScale.put(31, 100);
    }
    @PostMapping("/submit/option")
    @HandleDataActionExceptions
    public ResponseEntity<FeedbackForOptionSubmissionDTO> submitOption(@RequestBody List<TaskForOptionSubmittingDTO> list)
            throws NoSuchModuleInDB, NoSuchTaskInDB {
        int result = 0;
        List<Integer> modulesDone = new ArrayList<>();
        for(TaskForOptionSubmittingDTO answerObject: list){
            Module module = manager.getModuleById(answerObject.getModule());
            if(module.getVerifiable()){
                Task task = manager.getTaskById(answerObject.getId());
                if(task.getAnswer().equals(answerObject.getAnswer())){
                    result += module.getMaxPoints();
                    modulesDone.add(module.getId());
                }
            }else{
                int score = answerObject.getScore();
                if(score != 0){
                    result += score;
                    modulesDone.add(module.getId());
                }
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(FeedbackForOptionSubmissionDTO.builder()
                .score(translationScale.get(result)).modulesDone(modulesDone).build());
    }
}
