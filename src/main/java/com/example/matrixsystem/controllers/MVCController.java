package com.example.matrixsystem.controllers;

import com.example.matrixsystem.beans.DatabaseManager;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/app")
public class MVCController {

    private final DatabaseManager manager;
    @Autowired
    public MVCController(DatabaseManager manager){
        this.manager = manager;
    }

    @GetMapping("/all-tasks")
    public String allTasks(Model model){
        model.addAttribute("map", manager.getModuleTaskCounterMap());
        return "all-tasks";
    }

    @GetMapping("/all-tasks/module/{moduleNum}")
    public ModelAndView modulePage(@PathVariable int moduleNum, Model model) {
        try{
            model.addAttribute("moduleNum", moduleNum);
            model.addAttribute("moduleCapacity", manager.getModuleTaskCounterMap().get(moduleNum-1));
            return new ModelAndView("module-template", model.asMap());
        }catch (IndexOutOfBoundsException | NullPointerException e){
            model.addAttribute("error", "Задания с таким id нет в данном модуле");
            return new ModelAndView("error-view", model.asMap());
        }
    }
    @GetMapping("/management")
    public String addInformation(){
        return "management";
    }

}