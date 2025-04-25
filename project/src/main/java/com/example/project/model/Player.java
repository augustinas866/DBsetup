package com.example.project.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "player")
public class Player {
    @Id
    private Integer id;
    private String name;
    private String surname;
    private LocalDate dateOfBirth;
    private String personalCode;
} 