package com.example.matrixsystem.controllers;

import com.example.matrixsystem.dto.FeedbackForOptionSubmissionDTO;
import com.example.matrixsystem.dto.TaskForOptionSubmittingDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// в этом контроллере содержится логика оценки экзамена по профильной математике
// скорее всего эта логика будет меняться от года к году

@RestController
@RequestMapping("/api/v1/math")
public class MathExamLogicController {

    @PostMapping("/submit/option")
    public ResponseEntity<FeedbackForOptionSubmissionDTO> submitOption(List<TaskForOptionSubmittingDTO> list){
        System.out.println(list);
        return ResponseEntity.status(HttpStatus.OK).body(FeedbackForOptionSubmissionDTO.builder().build());
    }
}
