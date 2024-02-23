package com.example.matrixsystem.beans;

import com.example.matrixsystem.dto.ModuleDTO;
import com.example.matrixsystem.dto.TaskForOptionsDTO;
import com.example.matrixsystem.security.beans.UserInformation;
import com.example.matrixsystem.spring_data.entities.*;
import com.example.matrixsystem.spring_data.entities.Module;
import com.example.matrixsystem.spring_data.entities.enums.Roles;
import com.example.matrixsystem.spring_data.entities.enums.UserTaskRelationTypes;
import com.example.matrixsystem.spring_data.exceptions.*;
import com.example.matrixsystem.spring_data.interfaces.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.util.*;

@Service
@SessionScope
public class CommonDatabaseManager {
    private final TaskRepository taskRepository;
    private final ModuleRepository moduleRepository;
    private final UserRepository userRepository;
    private final UserTaskRepository userTaskRepository;
    private final UserInformation userInformation;
    private final SectionRepository sectionRepository;
    private final UserSectionRepository userSectionRepository;
    private final OptionTaskRepository optionTaskRepository;
    private final OptionRepository optionRepository;

    @Autowired
    public CommonDatabaseManager(TaskRepository taskRepository, ModuleRepository moduleRepository
            , UserRepository userRepository, UserTaskRepository userTaskRepository
            , UserInformation userInformation, SectionRepository sectionRepository
            , UserSectionRepository userSectionRepository, OptionTaskRepository optionTaskRepository
            , OptionRepository optionRepository) {
        this.taskRepository = taskRepository;
        this.moduleRepository = moduleRepository;
        this.userRepository = userRepository;
        this.userTaskRepository = userTaskRepository;
        this.userInformation = userInformation;
        this.sectionRepository = sectionRepository;
        this.userSectionRepository = userSectionRepository;
        this.optionTaskRepository = optionTaskRepository;
        this.optionRepository = optionRepository;
    }

    public List<Integer> getAllModuleTasksIds(Module module) {
        return taskRepository.getTasksByModule(module).stream().map(Task::getId).toList();
    }

    public HashMap<Integer, Integer> getModuleTaskCounterMap() {
        HashMap<Integer, Integer> toReturn = new HashMap<>();

        List<Module> modules = moduleRepository.findAll();

        for (Module module : modules) {
            toReturn.put(module.getId(), taskRepository.getTasksByModule(module).size());
        }
        return toReturn;
    }

    public Task getTaskById(Integer id) throws NoSuchTaskInDB {
        Optional<Task> optional = taskRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        } else {
            throw new NoSuchTaskInDB();
        }
    }

    public Integer getModuleCapacity(Module module) {
        return taskRepository.getTasksByModule(module).size();
    }

    public void addTask(Task task) throws ErrorCreatingTaskRecord {
        try {
            taskRepository.save(task);
        } catch (Exception e) {
            throw new ErrorCreatingTaskRecord();
        }
    }

    public Module getModuleById(Integer id) throws NoSuchModuleInDB {
        Optional<Module> optionalModule = moduleRepository.findById(id);
        if (optionalModule.isPresent()) return optionalModule.get();
        throw new NoSuchModuleInDB();
    }

    public Roles getUserRoleByLogin(String login) {
        return userRepository.findByLogin(login).getRole();
    }

    public Users getUserByLogin(String login) throws NoSuchUserInDB {
        Users user = userRepository.findByLogin(login);
        if (user == null) {
            throw new NoSuchUserInDB();
        }
        return user;
    }

    public void addUser(Users user) throws ErrorCreatingUserRecord {
        try {
            userRepository.save(user);
        } catch (Exception e) {
            throw new ErrorCreatingUserRecord();
        }
    }

    public void addUserTaskRelation(Users user, Task task, UserTaskRelationTypes relationType) throws ErrorCreatingUserTaskRecord {
        try {
            UserTask userTask = UserTask.builder().taskReference(task)
                    .userReference(user).relationType(relationType).build();
            this.userTaskRepository.save(userTask);
        } catch (Exception e) {
            throw new ErrorCreatingUserTaskRecord();
        }
    }

    public List<UserTask> getUserTasksByUserReference(Users user) throws NoSuchUserTaskRelation {
        try {
            return userTaskRepository.getUserTasksByUserReference(user);
        } catch (Exception e) {
            throw new NoSuchUserTaskRelation();
        }
    }

    public List<Task> getTasksByModule(Module module) throws NoSuchTaskInDB {
        try {
            return taskRepository.getTasksByModule(module);
        } catch (Exception e) {
            throw new NoSuchTaskInDB();
        }
    }

    public void deleteUserTask(UserTask userTask) throws ErrorDeletingUserTaskRecord {
        try {
            userTaskRepository.delete(userTask);
        } catch (Exception e) {
            throw new ErrorDeletingUserTaskRecord();
        }

    }

    @Transactional
    public void deleteTask(Task task) throws ErrorDeletingTask {
        try {

            List<OptionTask> listOption = optionTaskRepository.getOptionTaskByTask(task);

            if(listOption.size() != 0){
                Option optionLink = listOption.get(0).getOption();
                optionTaskRepository.deleteAllByOption(optionLink);
                optionRepository.delete(optionLink);
            }
            userTaskRepository.deleteAllByTaskReference(task);
            taskRepository.delete(task);

        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorDeletingTask();
        }
    }

    public UserTask getCurrentUserTaskByTask(Task task) throws NoSuchUserInDB, NoSuchUserTaskRelation {
        List<UserTask> userTaskList = getUserTasksByUserReference(userInformation.getUser());

        for (UserTask userTask : userTaskList) {
            Integer taskIdOfPair = userTask.getTaskReference().getId();

            if (taskIdOfPair.equals(task.getId())) {
                return userTask;
            }
        }
        throw new NoSuchUserTaskRelation();
    }

    // возвращает статус связи текущего пользователя и переданного задания
    public UserTaskRelationTypes getUserTaskRelationByTask(Task task) throws NoSuchUserInDB, NoSuchUserTaskRelation {
        List<UserTask> userTaskList = getUserTasksByUserReference(userInformation.getUser());

        for (UserTask userTask : userTaskList) {
            Integer taskIdOfPair = userTask.getTaskReference().getId();

            if (taskIdOfPair.equals(task.getId())) {
                return userTask.getRelationType();
            }
        }
        return UserTaskRelationTypes.NONE;
    }

    // удаляет связь user-task текущего пользователя с переданным в параметре заданием
    public void deleteUserTaskRelationOfCurrentUserByTask(Task task) throws NoSuchUserTaskRelation
            , NoSuchUserInDB, ErrorDeletingUserTaskRecord {
        this.deleteUserTask(getCurrentUserTaskByTask(task));
    }

    public Integer counterOfDoneCurrentUserTaskRelationsDone(Module module) throws NoSuchUserInDB {
        List<Task> tasks = taskRepository.getTasksByModule(module);

        int counter = 0;
        for (Task task : tasks) {
            try {
                if (getUserTaskRelationByTask(task).equals(UserTaskRelationTypes.DONE)) {
                    counter++;
                }
            } catch (NoSuchUserTaskRelation ignored) {
            }
        }
        return counter;
    }

    public HashMap<Integer, Integer> getDoneMap() throws NoSuchUserInDB {
        List<Module> listModules = moduleRepository.findAll();
        HashMap<Integer, Integer> doneMap = new HashMap<>();
        for (Module module : listModules) {
            doneMap.put(module.getId(), counterOfDoneCurrentUserTaskRelationsDone(module));
        }
        return doneMap;
    }

    public HashMap<Integer, String> getModuleNameMap() {
        HashMap<Integer, String> moduleNameMap = new HashMap<>();
        moduleRepository.findAll().forEach((module) -> {
            moduleNameMap.put(module.getId(), module.getName());
        });
        return moduleNameMap;
    }

    public List<Module> getAllModules() {
        return moduleRepository.findAll();
    }

    public List<TaskForOptionsDTO> generateOption() {
        List<TaskForOptionsDTO> toReturn = new ArrayList<>();
        for (Module module : moduleRepository.findAll()) {
            List<Task> list = taskRepository.getTasksByModule(module);
            Random random = new Random();
            int randomIndex = random.nextInt(list.size());
            Task randomTask = list.get(randomIndex);
            Module randomTaskModule = randomTask.getModule();
            TaskForOptionsDTO randomTaskDTO = TaskForOptionsDTO.builder().id(randomTask.getId())
                    .module(ModuleDTO.builder().id(randomTaskModule.getId())
                            .verifiable(randomTaskModule.getVerifiable()).maxPoints(module.getMaxPoints())
                            .build()).task(randomTask.getTask()).solution(randomTask.getSolution())
                    .answer(randomTask.getAnswer()).img(randomTask.getImg()).build();

            toReturn.add(randomTaskDTO);
        }
        return toReturn;
    }

    public Option getOptionById(Integer id) throws NoSuchOptionException {
        try {
            return optionRepository.getOptionById(id);
        } catch (Exception e) {
            throw new NoSuchOptionException();
        }
    }

    public List<TaskForOptionsDTO> getOption(Option option) {
        List<TaskForOptionsDTO> toReturn = new ArrayList<>();
        for (OptionTask optionTask : optionTaskRepository.getOptionTaskByOption(option)) {
            Task task = optionTask.getTask();
            TaskForOptionsDTO taskDTO = TaskForOptionsDTO.builder().id(task.getId())
                    .module(ModuleDTO.builder().id(task.getModule().getId())
                            .verifiable(task.getModule().getVerifiable()).maxPoints(task.getModule().getMaxPoints())
                            .build()).task(task.getTask()).solution(task.getSolution())
                    .answer(task.getAnswer()).img(task.getImg()).build();
            toReturn.add(taskDTO);
        }
        return toReturn;
    }

    public void addSection(Section section) throws ErrorCreatingSection {
        try {
            sectionRepository.save(section);
        } catch (Exception e) {
            throw new ErrorCreatingSection();
        }
    }

    public void deleteSection(Section section) throws ErrorDeletingSection {
        try {
            sectionRepository.delete(section);
        } catch (Exception e) {
            throw new ErrorDeletingSection();
        }
    }

    public Section getSectionById(Integer id) throws NoSuchSectionException {
        try {
            return sectionRepository.getSectionsById(id);
        } catch (Exception e) {
            throw new NoSuchSectionException();
        }
    }

    public List<Section> getSectionsOfCurrentUser() throws NoSuchUserInDB {
        List<Section> toReturn = new ArrayList<>();
        List<UserSection> userSectionsRecords =
                userSectionRepository.getUserSectionByUser(userInformation.getUser());
        for (UserSection userSection : userSectionsRecords) {
            toReturn.add(userSection.getSection());
        }
        return toReturn;
    }

    public void addUserSection(UserSection userSection) throws ErrorCreatingUserSectionRecord {
        try {
            userSectionRepository.save(userSection);
        } catch (Exception e) {
            throw new ErrorCreatingUserSectionRecord();
        }
    }

    public void deleteUserSection(UserSection userSection) throws ErrorDeletingUserSection {
        try {
            userSectionRepository.delete(userSection);
        } catch (Exception e) {
            throw new ErrorDeletingUserSection();
        }
    }

    public UserSection getUserSectionOfCurrentUserBySection(Section section) throws NoSuchUserSectionRecord {
        try {
            return userSectionRepository.getUserSectionBySectionAndUser(section, userInformation.getUser());
        } catch (Exception e) {
            throw new NoSuchUserSectionRecord();
        }

    }

    public void addOption(Option option) throws ErrorCreatingOption {
        try {
            optionRepository.save(option);
        } catch (Exception e) {
            throw new ErrorCreatingOption();
        }

    }

    public void addTaskToOption(Option option, Task task) {
        optionTaskRepository.save(OptionTask.builder()
                .option(option).task(task).build());
    }
}
