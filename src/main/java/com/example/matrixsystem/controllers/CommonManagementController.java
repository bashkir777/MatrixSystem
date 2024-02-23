package com.example.matrixsystem.controllers;

import com.example.matrixsystem.beans.CommonDatabaseManager;
import com.example.matrixsystem.dto.*;
import com.example.matrixsystem.security.beans.UserInformation;
import com.example.matrixsystem.spring_data.entities.*;
import com.example.matrixsystem.spring_data.entities.Module;
import com.example.matrixsystem.spring_data.exceptions.*;
import com.example.matrixsystem.security.annotations.RolesAllowed;
import com.example.matrixsystem.spring_data.annotations.HandleDataActionExceptions;
import com.example.matrixsystem.spring_data.entities.enums.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

// этот контроллер содержит методы, посредством которых осуществляется весь менеджмент
// добавление студентов, учителей, заданий и тп.
@RestController
@RequestMapping("/api/v1/management")
public class CommonManagementController {
    private final CommonDatabaseManager manager;
    private final UserInformation userInformation;

    @Autowired
    public CommonManagementController(CommonDatabaseManager manager, UserInformation userInformation) {
        this.manager = manager;
        this.userInformation = userInformation;
    }

    @PostMapping("/add/task/common-pull")
    @RolesAllowed("GOD")
    @HandleDataActionExceptions
    public ResponseEntity<String> addNewTaskToTheCommonPull(@RequestParam("task") String task,
                                                            @RequestParam("answer") String answer,
                                                            @RequestParam(value = "solution", required = false) String solution,
                                                            @RequestParam("module") Integer module,
                                                            @RequestPart(value = "image", required = false) MultipartFile image)
            throws NoSuchModuleInDB, ErrorCreatingTaskRecord, IOException {
        Task.TaskBuilder builder = Task.builder();
        if (image != null) {
            String fileName = UUID.randomUUID() + "." + Objects.requireNonNull(image.getContentType()).split("/")[1];
            String uploadDir = "db/images/";
            Path filePath = Paths.get(uploadDir, fileName);
            Files.write(filePath, image.getBytes());
            builder.img(filePath.toString());
        }
        if(solution != null){
            builder.solution(solution);
        }

        Module moduleObj;

        moduleObj = manager.getModuleById(module);

        Task taskObj = builder.task(task)
                .answer(answer)
                .module(moduleObj).build();

        manager.addTask(taskObj);

        return ResponseEntity.status(HttpStatus.CREATED).body("Задание успешно добавлено");
    }

    private ResponseEntity<String> addUserLogic(UserDTO userDTO, Roles role)
            throws ErrorCreatingUserRecord{
        Users user = Users.builder().login(userDTO.getLogin())
                .role(role).password(userDTO.getPassword()).build();
        manager.addUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(role + " успешно добавлен");
    }

    @PostMapping("/toggle/user-section/{id}")
    @HandleDataActionExceptions
    public ResponseEntity<String> addUserSection(@PathVariable Integer id) throws NoSuchUserInDB, NoSuchSectionException, ErrorCreatingUserSectionRecord, NoSuchUserSectionRecord, ErrorDeletingUserSection {
        boolean hasRead = false;
        for( Section section: manager.getSectionsOfCurrentUser()){
            if (section.getId().equals(id)) {
                hasRead = true;
                break;
            }
        }
        if(!hasRead){
            manager.addUserSection(
                    UserSection.builder().user(userInformation.getUser())
                            .section(manager.getSectionById(id)).build());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("отношение user-section успешно добавлено");
        }else{
            manager.deleteUserSection(manager
                    .getUserSectionOfCurrentUserBySection(manager.getSectionById(id)));
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body("отношение user-section успешно удалено");
        }

    }

    @PostMapping("/add/option")
    @RolesAllowed({"GOD", "TEACHER"})
    @HandleDataActionExceptions
    public ResponseEntity<OptionIdDTO> addNewCustomTask(@RequestBody List<ModuleTaskDTO> optionDTO)
            throws ErrorCreatingOption, NoSuchTaskInDB {
        Option option = Option.builder().build();
        manager.addOption(option);
        for(ModuleTaskDTO dto: optionDTO){
            manager.addTaskToOption(option, manager.getTaskById(dto.getTaskId()));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(OptionIdDTO.builder().optionId(option.getId()).build());
    }

    @PostMapping("/add/user/student")
    @RolesAllowed({"GOD", "TEACHER"})
    @HandleDataActionExceptions
    public ResponseEntity<String> addNewStudent(@RequestBody UserDTO userDTO) throws ErrorCreatingUserRecord{
        return addUserLogic(userDTO, Roles.STUDENT);
    }

    @PostMapping("/add/user/teacher")
    @RolesAllowed({"GOD", "MANAGER"})
    @HandleDataActionExceptions
    public ResponseEntity<String> addNewTeacher(@RequestBody UserDTO userDTO) throws ErrorCreatingUserRecord{
        return addUserLogic(userDTO, Roles.TEACHER);
    }

    @PostMapping("/add/user/manager")
    @RolesAllowed("GOD")
    @HandleDataActionExceptions
    public ResponseEntity<String> addNewManager(@RequestBody UserDTO userDTO) throws ErrorCreatingUserRecord{
        return addUserLogic(userDTO, Roles.MANAGER);
    }

    @PostMapping("/add/user/god")
    @RolesAllowed("GOD")
    @HandleDataActionExceptions
    public ResponseEntity<String> addNewGod(@RequestBody UserDTO userDTO) throws ErrorCreatingUserRecord{
        return addUserLogic(userDTO, Roles.GOD);
    }

    @PostMapping("/add/section")
    @RolesAllowed("GOD")
    @HandleDataActionExceptions
    public ResponseEntity<String> addNewSection(@RequestBody SectionDTO sectionDTO) throws NoSuchModuleInDB
            , ErrorCreatingSection {
        Section section = Section.builder().link(sectionDTO.getLink()).name(sectionDTO.getName())
                .module(manager.getModuleById(sectionDTO.getModule()))
                .visibleForStudent(sectionDTO.getVisibleForStudent()).build();
        manager.addSection(section);
        return ResponseEntity.status(HttpStatus.CREATED).body("Секция успешно создана");
    }
    @PatchMapping("/update/section/{id}")
    @RolesAllowed("GOD")
    @HandleDataActionExceptions
    public ResponseEntity<String> updateSection(@PathVariable Integer id, @RequestBody SectionDTOForUpdating sectionDTO) throws NoSuchModuleInDB
            , ErrorCreatingSection, NoSuchSectionException {
        Section section = manager.getSectionById(id);
        section.setName(sectionDTO.getName());
        section.setLink(sectionDTO.getLink());
        section.setVisibleForStudent(sectionDTO.getVisibleForStudent());
        manager.addSection(section);
        return ResponseEntity.status(HttpStatus.CREATED).body("Секция успешно обновлена");
    }
    @DeleteMapping("/delete/section/{id}")
    @RolesAllowed("GOD")
    @HandleDataActionExceptions
    public ResponseEntity<String> deleteSectionById(@PathVariable Integer id) throws NoSuchSectionException
            , ErrorDeletingSection {
        manager.deleteSection(manager.getSectionById(id));
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Секция успешно удалена");
    }
    @DeleteMapping("/delete/task/{id}")
    @RolesAllowed("GOD")
    @HandleDataActionExceptions
    public ResponseEntity<String> deleteTaskById(@PathVariable Integer id) throws
            NoSuchTaskInDB, ErrorDeletingTask {
        manager.deleteTask(manager.getTaskById(id));
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("задание успешно удалено");
    }
}
