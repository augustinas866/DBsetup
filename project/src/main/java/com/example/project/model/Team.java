package com.example.project.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "team")
public class Team {
    @Id
    private Integer teamId;
    
    @ManyToOne
    @JoinColumn(name = "coach_id")
    private Coach coach;
    
    @ManyToOne
    @JoinColumn(name = "player_id")
    private Player player;
    
    private String teamName;
    private Integer yearCreated;
} 