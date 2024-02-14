package com.example.matrixsystem.spring_data.entities;

import com.example.matrixsystem.spring_data.entities.enums.Roles;
import com.sun.istack.internal.NotNull;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotNull
    private String login;
    @NotNull
    private String password;

    @Enumerated(EnumType.STRING)
    private Roles role;

    @OneToMany(mappedBy = "user")
    private Set<UserSection> userSections = new HashSet<>();

    @OneToMany(mappedBy = "userReference", fetch = FetchType.EAGER)
    private Set<UserTask> userTask = new HashSet<>();
}
