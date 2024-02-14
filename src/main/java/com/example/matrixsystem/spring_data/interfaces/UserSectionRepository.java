package com.example.matrixsystem.spring_data.interfaces;

import com.example.matrixsystem.spring_data.entities.Section;
import com.example.matrixsystem.spring_data.entities.UserSection;
import com.example.matrixsystem.spring_data.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserSectionRepository extends JpaRepository<UserSection, Integer> {
    List<UserSection> getUserSectionByUser(Users user);
    UserSection getUserSectionBySectionAndUser(Section section, Users user);
}
