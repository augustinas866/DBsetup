package com.example.project.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "coach")
public class Coach {
    @Id
    private Integer id;
    private String name;
    private String surname;
    private LocalDate coachingFrom;
    private String licenseId;
    private String personalCode;
} 