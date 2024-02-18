package com.example.matrixsystem.spring_data.interfaces;

import com.example.matrixsystem.spring_data.entities.Option;
import com.example.matrixsystem.spring_data.entities.OptionTask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OptionTaskRepository extends JpaRepository<OptionTask, Integer> {
    List<OptionTask> getOptionTaskByOption(Option option);
}
