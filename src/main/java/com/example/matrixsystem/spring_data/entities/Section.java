package com.example.matrixsystem.spring_data.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @ManyToOne
    @JoinColumn(referencedColumnName = "id")
    @JsonBackReference
    private Module module;
}
