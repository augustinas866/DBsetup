package com.example.project.controller;

import com.example.project.dto.PlayerDto;
import com.example.project.dto.CoachDto;
import com.example.project.dto.TeamDto;
import com.example.project.mapper.EntityMapper;
import com.example.project.model.Player;
import com.example.project.model.Coach;
import com.example.project.model.Team;
import com.example.project.repository.PlayerRepository;
import com.example.project.repository.CoachRepository;
import com.example.project.repository.TeamRepository;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestController {

    private final PlayerRepository playerRepository;
    private final CoachRepository coachRepository;
    private final TeamRepository teamRepository;
    private final EntityMapper mapper;

    @PostMapping("/player")
    public PlayerDto.Response createPlayer(@RequestBody PlayerDto.Request request) {
        try {
            log.info("Creating player: {}", request);
            Player player = mapper.toEntity(request);
            log.info("Mapped to entity: {}", player);
            player = playerRepository.save(player);
            log.info("Saved player: {}", player);
            return mapper.toDto(player);
        } catch (Exception e) {
            log.error("Error creating player", e);
            throw e;
        }
    }

    @PostMapping("/coach")
    public CoachDto.Response createCoach(@RequestBody CoachDto.Request request) {
        try {
            Coach coach = mapper.toEntity(request);
            coach = coachRepository.save(coach);
            return mapper.toDto(coach);
        } catch (Exception e) {
            log.error("Error creating coach", e);
            throw e;
        }
    }

    @PostMapping("/team")
    public TeamDto.Response createTeam(@RequestBody TeamDto.Request request) {
        try {
            log.info("Creating team with request: {}", request);
            
            // Validate required fields
            if (request.getTeamName() == null || request.getYearCreated() == null) {
                throw new IllegalArgumentException("Team name and year created are required");
            }
            
            // Create team entity
            Team team = mapper.toEntity(request);
            log.info("Mapped to team entity: {}", team);
            
            // Set coach if provided
            if (request.getCoachId() != null) {
                log.info("Finding coach with id: {}", request.getCoachId());
                Coach coach = coachRepository.findById(request.getCoachId())
                    .orElseThrow(() -> new IllegalArgumentException("Coach not found with id: " + request.getCoachId()));
                team.setCoach(coach);
                log.info("Set coach: {}", coach);
            }
            
            // Set player if provided
            if (request.getPlayerId() != null) {
                log.info("Finding player with id: {}", request.getPlayerId());
                Player player = playerRepository.findById(request.getPlayerId())
                    .orElseThrow(() -> new IllegalArgumentException("Player not found with id: " + request.getPlayerId()));
                team.setPlayer(player);
                log.info("Set player: {}", player);
            }
            
            // Save team
            log.info("Saving team: {}", team);
            team = teamRepository.save(team);
            log.info("Successfully saved team: {}", team);
            
            TeamDto.Response response = mapper.toDto(team);
            log.info("Mapped to response: {}", response);
            return response;
        } catch (Exception e) {
            log.error("Error creating team: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create team: " + e.getMessage(), e);
        }
    }

    @GetMapping("/player/{id}")
    public PlayerDto.Response getPlayer(@PathVariable Integer id) {
        return playerRepository.findById(id)
                .map(mapper::toDto)
                .orElse(null);
    }

    @GetMapping("/coach/{id}")
    public CoachDto.Response getCoach(@PathVariable Integer id) {
        return coachRepository.findById(id)
                .map(mapper::toDto)
                .orElse(null);
    }

    @GetMapping("/team/{id}")
    public TeamDto.Response getTeam(@PathVariable Integer id) {
        return teamRepository.findById(id)
                .map(mapper::toDto)
                .orElse(null);
    }
} 