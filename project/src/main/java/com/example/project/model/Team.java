package com.example.project.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "team")
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer teamId;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "coach_id")
    private Coach coach;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "player_id")
    private Player player;
    
    private String teamName;
    private Integer yearCreated;
} 