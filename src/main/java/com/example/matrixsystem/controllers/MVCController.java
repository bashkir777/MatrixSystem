package com.example.matrixsystem.controllers;

import com.example.matrixsystem.beans.DatabaseManager;

import com.example.matrixsystem.spring_data.annotations.HandleDataActionExceptions;
import com.example.matrixsystem.spring_data.exceptions.NoSuchModuleInDB;
import com.example.matrixsystem.spring_data.exceptions.NoSuchUserInDB;
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
    public MVCController(DatabaseManager manager) {
        this.manager = manager;
    }

    @GetMapping("/all-tasks")
    @HandleDataActionExceptions
    public String allTasks(Model model) throws NoSuchUserInDB {
        model.addAttribute("map", manager.getModuleTaskCounterMap());
        model.addAttribute("done_map", manager.getDoneMap());
        model.addAttribute("module_name_map", manager.getModuleNameMap());
        model.addAttribute("moduleCounter", manager.getAllModules().size());
        return "all-tasks";
    }

    @GetMapping("/all-tasks/module/{id}")
    public ModelAndView modulePage(@PathVariable Integer id, Model model) throws NoSuchModuleInDB {
        model.addAttribute("moduleId", id);
        model.addAttribute("moduleCapacity", manager.getModuleCapacity(manager.getModuleById(id)));
        return new ModelAndView("module-template", model.asMap());
    }

    @GetMapping("/management")
    public String addInformation() {
        return "management";
    }

}
