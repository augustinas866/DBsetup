package com.example.project.controller;

import com.example.project.repository.PlayerRepository;
import com.example.project.repository.CoachRepository;
import com.example.project.repository.TeamRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DatabaseViewController {
    
    private final PlayerRepository playerRepository;
    private final CoachRepository coachRepository;
    private final TeamRepository teamRepository;
    
    @GetMapping("/players")
    public Object getAllPlayers() {
        return playerRepository.findAll();
    }
    
    @GetMapping("/coaches")
    public Object getAllCoaches() {
        return coachRepository.findAll();
    }
    
    @GetMapping("/teams")
    public Object getAllTeams() {
        return teamRepository.findAll();
    }
} 