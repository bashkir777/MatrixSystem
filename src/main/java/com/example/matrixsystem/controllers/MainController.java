package com.example.matrixsystem.controllers;

import com.example.matrixsystem.beans.DatabaseManager;
import com.example.matrixsystem.spring_data.entities.Task;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;


@Controller
@RequestMapping("/app")
public class MainController {

    private final DatabaseManager manager;
    @Autowired
    public MainController(DatabaseManager manager){
        this.manager = manager;
    }

    @GetMapping("/all-tasks")
    public String allTasks(Model model){
        model.addAttribute("map", manager.getModuleTaskCounterMap());

        return "all-tasks";
    }

    @GetMapping("/all-tasks/module/{moduleNum}/task/{taskNum}")
    public ModelAndView hello(@PathVariable int moduleNum, @PathVariable int taskNum, Model model) {
        ArrayList<Task> list = manager.getAllModuleTasks(moduleNum-1);
        try{
            Task task = list.get(taskNum-1);
            model.addAttribute("taskNum", taskNum);
            model.addAttribute("task", task);
            model.addAttribute("moduleNum", moduleNum);
            model.addAttribute("moduleCapacity", manager.getModuleTaskCounterMap().get(moduleNum - 1));
            return new ModelAndView("task-template", model.asMap());
        }catch (IndexOutOfBoundsException | NullPointerException e){
            model.addAttribute("error", "Задания с таким id нет в данном модуле");
            return new ModelAndView("error-view", model.asMap());
        }
    }
}
