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

    @GetMapping("/all-tasks/module/{num}")
    public String hello(@PathVariable Integer num, Model model) {
        model.addAttribute("module", num);
        return "module-template";
    }
}
