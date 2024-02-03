package com.example.matrixsystem.spring_data.entities;

import com.sun.istack.internal.NotNull;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotNull
    private String login;
    @NotNull
    private String password;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "authority", referencedColumnName="id")
    private Authority role;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_tasks",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "task_id", referencedColumnName = "id")
    )
    private Set<Task> tasks = new HashSet<>();
}
