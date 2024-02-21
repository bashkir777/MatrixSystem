package com.example.matrixsystem.spring_data.interfaces;

import com.example.matrixsystem.spring_data.entities.HomeworkCustomTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface HomeworkCustomTaskRepository extends JpaRepository<HomeworkCustomTask, Integer> {
}
