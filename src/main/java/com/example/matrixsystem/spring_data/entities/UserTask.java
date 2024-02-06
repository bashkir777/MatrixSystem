package com.example.matrixsystem.spring_data.entities;

import com.example.matrixsystem.spring_data.entities.enums.UserTaskRelationTypes;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(uniqueConstraints = @UniqueConstraint(columnNames={"userReference", "taskReference"}))
@Builder
public class UserTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name="taskReference", referencedColumnName = "id")
    private Task taskReference;
    @ManyToOne
    @JoinColumn(name="userReference", referencedColumnName = "id")
    private Users userReference;

    @Enumerated(EnumType.STRING)
    private UserTaskRelationTypes relationType;
}