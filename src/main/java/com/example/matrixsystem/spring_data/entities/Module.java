package com.example.matrixsystem.spring_data.entities;

import com.example.matrixsystem.spring_data.entities.enums.Part;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name="module")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Module {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(10) DEFAULT 'FIRST'")
    private Part part;

    @Column(columnDefinition = "integer DEFAULT 1")
    private Integer maxPoints;

    @Column(columnDefinition = "BOOLEAN DEFAULT 'TRUE'")
    private Boolean verifiable;

    @OneToMany(mappedBy = "module", fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<Section> sections = new ArrayList<>();
}
