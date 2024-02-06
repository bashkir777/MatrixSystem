package com.example.matrixsystem.spring_data.interfaces;

import com.example.matrixsystem.spring_data.entities.UserTask;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTaskRepository extends JpaRepository<UserTask, Integer> {
}
