package com.example.matrixsystem.spring_data.interfaces;

import com.example.matrixsystem.spring_data.entities.Task;
import com.example.matrixsystem.spring_data.entities.UserTask;
import com.example.matrixsystem.spring_data.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserTaskRepository extends JpaRepository<UserTask, Integer> {
    List<UserTask> getUserTasksByUserReference(Users user);
    void deleteAllByTaskReference(Task task);
    void deleteAllByUserReference(Users user);
}
