package com.example.matrixsystem.spring_data.interfaces;

import com.example.matrixsystem.spring_data.entities.UserCustomTask;
import com.example.matrixsystem.spring_data.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface UserCustomTaskRepository extends JpaRepository<UserCustomTask, Integer> {
    List<UserCustomTask> getUserCustomTaskByUserReference(Users users);
    void deleteAllByUserReference(Users user);
}
