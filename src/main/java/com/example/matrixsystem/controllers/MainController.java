package com.example.matrixsystem.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/app")
public class MainController {

    @GetMapping("/all-tasks")
    public String hello(){
        return "all-tasks";
    }

    @GetMapping("/all-tasks/module/{moduleNum}/task/{taskNum}")
    public String hello(@PathVariable Integer moduleNum,@PathVariable Integer taskNum, Model model) {
        model.addAttribute("module", moduleNum);
        model.addAttribute("task", taskNum);
        return "task-template";
    }
}
