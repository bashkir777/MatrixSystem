package com.example.matrixsystem.controllers;

import com.example.matrixsystem.beans.DatabaseManager;

import com.example.matrixsystem.security.beans.UserInformation;
import com.example.matrixsystem.spring_data.annotations.HandleDataActionExceptions;
import com.example.matrixsystem.spring_data.entities.Module;
import com.example.matrixsystem.spring_data.entities.enums.Roles;
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
    private final UserInformation userInformation;
    @Autowired
    public MVCController(DatabaseManager manager, UserInformation userInformation) {
        this.manager = manager;
        this.userInformation = userInformation;
    }

    @GetMapping("/all-tasks")
    @HandleDataActionExceptions
    public String allTasks(Model model) throws NoSuchUserInDB {
        model.addAttribute("role", userInformation.getUserRole().name());
        model.addAttribute("map", manager.getModuleTaskCounterMap());
        model.addAttribute("done_map", manager.getDoneMap());
        model.addAttribute("module_name_map", manager.getModuleNameMap());
        model.addAttribute("moduleCounter", manager.getAllModules().size());

        int firstModuleFromSecondPart = 0;
        for(Module module: manager.getAllModules()){
            if(module.getPart().name().equals("SECOND")){
                firstModuleFromSecondPart = module.getId();
                break;
            }
        }
        model.addAttribute("firstModuleFromSecondPart", firstModuleFromSecondPart);
        return "all-tasks";
    }
    @GetMapping("/theory")
    @HandleDataActionExceptions
    public String theory(Model model) throws NoSuchUserInDB {
        model.addAttribute("moduleList", manager.getAllModules());
        model.addAttribute("role", userInformation.getUserRole().name());
        model.addAttribute("readSections", manager.getSectionsOfCurrentUser());
        return "theory";
    }

    @GetMapping("/all-tasks/module/{id}")
    public ModelAndView modulePage(@PathVariable Integer id, Model model) throws NoSuchModuleInDB {
        model.addAttribute("role", userInformation.getUserRole().name());
        model.addAttribute("moduleId", id);
        model.addAttribute("moduleCapacity", manager.getModuleCapacity(manager.getModuleById(id)));
        return new ModelAndView("module-template", model.asMap());
    }

    @GetMapping("/management")
    public String addInformation(Model model) {
        model.addAttribute("role", userInformation.getUserRole().name());
        return "management";
    }

    @GetMapping("/options")
    @HandleDataActionExceptions
    public String options(Model model) {
        model.addAttribute("role", userInformation.getUserRole().name());
        return "choose-option-type-template";
    }
    @GetMapping("/options/autogenerated")
    @HandleDataActionExceptions
    public String autogeneratedOption(Model model) {
        model.addAttribute("role", userInformation.getUserRole().name());
        return "option-template";
    }

}
