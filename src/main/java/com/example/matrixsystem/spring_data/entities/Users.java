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

    @ManyToMany
    @JoinTable(
            name = "user_section",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "section_id", referencedColumnName = "id")
    )
    private Set<Section> sections = new HashSet<>();

    @OneToMany(mappedBy = "userReference", fetch = FetchType.EAGER)
    private Set<UserTask> userTask = new HashSet<>();
}
