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
    @DeleteMapping("/delete/user/by-info")
    @RolesAllowed("GOD")
    @HandleDataActionExceptions
    public ResponseEntity<String> deleteUserByLogin(@RequestParam("name") String name,
                                                    @RequestParam("surname") String surname){
        manager.deleteUser(manager.getUserByNameAndSurname(name, surname));
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Пользователь успешно удален");
    }
    @DeleteMapping("/delete/user/by-login")
    @RolesAllowed("GOD")
    @HandleDataActionExceptions
    public ResponseEntity<String> deleteUserByLogin(@RequestParam("login") String login)
            throws NoSuchUserInDB {
        manager.deleteUser(manager.getUserByLogin(login));
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Пользователь успешно удален");
    }
    @PostMapping("/add/user")
    @RolesAllowed("GOD")
    @HandleDataActionExceptions
    public ResponseEntity<String> addNewStudent(@RequestParam("login") String login,
                                                @RequestParam("password") String password,
                                                @RequestParam(value = "phoneNumber", required = false) String phoneNumber,
                                                @RequestParam(value = "telegram", required = false) String telegram,
                                                @RequestPart("name") String name,
                                                @RequestPart("surname") String surname,
                                                @RequestPart("role") String role,
                                                @RequestPart("miroBoard") String miroBoard) throws ErrorCreatingUserRecord {
        Users.UsersBuilder builder = Users.builder();
        builder.name(name).surname(surname).login(login).password(password).miroBoard(miroBoard);

        if(role.toUpperCase().equals(Roles.STUDENT.name()) ||
                role.toUpperCase().equals(Roles.GOD.name()) ||
                role.toUpperCase().equals(Roles.TEACHER.name())){
            builder.role(Roles.valueOf(role.toUpperCase()));
        }else{
            throw new ErrorCreatingUserRecord();
        }
        if(phoneNumber != null){
            builder.phoneNumber(phoneNumber);
        }
        if(telegram != null){
            builder.telegram(telegram);
        }

        manager.addUser(builder.build());

        return ResponseEntity.status(HttpStatus.CREATED).body("Пользователь успешно создан");
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
