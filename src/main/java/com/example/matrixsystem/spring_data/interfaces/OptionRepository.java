package com.example.matrixsystem.spring_data.interfaces;

import com.example.matrixsystem.spring_data.entities.Option;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OptionRepository extends JpaRepository<Option, Integer> {
    Option getOptionById(Integer id);
}
