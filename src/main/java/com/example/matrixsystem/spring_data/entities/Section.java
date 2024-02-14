package com.example.matrixsystem.spring_data.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Section {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(length = Integer.MAX_VALUE)
    private String name;

    @Column(length = Integer.MAX_VALUE)
    private String link;

    @Column(columnDefinition = "boolean DEFAULT true")
    private Boolean visibleForStudent;

    @ManyToMany(mappedBy = "sections")
    private Set<Users> users = new HashSet<>();

    @ManyToOne
    @JoinColumn(referencedColumnName = "id")
    @JsonBackReference
    private Module module;
}
