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
import org.springframework.http.ResponseEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DatabaseViewController {
    
    private final PlayerRepository playerRepository;
    private final CoachRepository coachRepository;
    private final TeamRepository teamRepository;
    private final EntityMapper mapper;
    
    // Player operations
    @GetMapping("/players")
    public List<PlayerDto.Response> getAllPlayers(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String surname,
            @RequestParam(required = false) String personalCode) {
        log.info("Fetching players with filters - name: {}, surname: {}, personalCode: {}", name, surname, personalCode);
        
        return playerRepository.findAll().stream()
            .filter(player -> name == null || player.getName().toLowerCase().contains(name.toLowerCase()))
            .filter(player -> surname == null || player.getSurname().toLowerCase().contains(surname.toLowerCase()))
            .filter(player -> personalCode == null || player.getPersonalCode().equals(personalCode))
            .map(mapper::toDto)
            .collect(Collectors.toList());
    }

    @PostMapping("/players")
    public PlayerDto.Response createPlayer(@RequestBody PlayerDto.Request request) {
        log.info("Creating player: {}", request);
        Player player = mapper.toEntity(request);
        player = playerRepository.save(player);
        return mapper.toDto(player);
    }

    @GetMapping("/players/{id}")
    public PlayerDto.Response getPlayer(@PathVariable Integer id) {
        log.info("Fetching player with id: {}", id);
        return playerRepository.findById(id)
            .map(mapper::toDto)
            .orElse(null);
    }

    @PutMapping("/players/{id}")
    public PlayerDto.Response updatePlayer(@PathVariable Integer id, @RequestBody PlayerDto.Request request) {
        log.info("Updating player with id: {}", id);
        Player player = mapper.toEntity(request);
        player.setId(id);
        Player updatedPlayer = playerRepository.save(player);
        return mapper.toDto(updatedPlayer);
    }

    @DeleteMapping("/players/{id}")
    public ResponseEntity<Void> deletePlayer(@PathVariable Integer id) {
        log.info("Deleting player with id: {}", id);
        try {
            // Check if player exists
            if (!playerRepository.existsById(id)) {
                return ResponseEntity.notFound().build();
            }

            // Check if player is referenced by any team
            List<Team> teamsWithPlayer = teamRepository.findAll().stream()
                .filter(team -> team.getPlayer() != null && team.getPlayer().getId().equals(id))
                .collect(Collectors.toList());

            if (!teamsWithPlayer.isEmpty()) {
                // Remove player reference from teams
                for (Team team : teamsWithPlayer) {
                    team.setPlayer(null);
                    teamRepository.save(team);
                }
            }

            // Delete the player
            playerRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error deleting player: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    // Coach operations
    @GetMapping("/coaches")
    public List<CoachDto.Response> getAllCoaches(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String surname,
            @RequestParam(required = false) String licenseId,
            @RequestParam(required = false) String personalCode) {
        log.info("Fetching coaches with filters - name: {}, surname: {}, licenseId: {}, personalCode: {}", 
                name, surname, licenseId, personalCode);
        
        return coachRepository.findAll().stream()
            .filter(coach -> name == null || coach.getName().toLowerCase().contains(name.toLowerCase()))
            .filter(coach -> surname == null || coach.getSurname().toLowerCase().contains(surname.toLowerCase()))
            .filter(coach -> licenseId == null || coach.getLicenseId().equals(licenseId))
            .filter(coach -> personalCode == null || coach.getPersonalCode().equals(personalCode))
            .map(mapper::toDto)
            .collect(Collectors.toList());
    }

    @PostMapping("/coaches")
    public CoachDto.Response createCoach(@RequestBody CoachDto.Request request) {
        log.info("Creating coach: {}", request);
        Coach coach = mapper.toEntity(request);
        coach = coachRepository.save(coach);
        return mapper.toDto(coach);
    }

    @GetMapping("/coaches/{id}")
    public CoachDto.Response getCoach(@PathVariable Integer id) {
        log.info("Fetching coach with id: {}", id);
        return coachRepository.findById(id)
            .map(mapper::toDto)
            .orElse(null);
    }

    @PutMapping("/coaches/{id}")
    public CoachDto.Response updateCoach(@PathVariable Integer id, @RequestBody CoachDto.Request request) {
        log.info("Updating coach with id: {}", id);
        Coach coach = mapper.toEntity(request);
        coach.setId(id);
        Coach updatedCoach = coachRepository.save(coach);
        return mapper.toDto(updatedCoach);
    }

    @DeleteMapping("/coaches/{id}")
    public ResponseEntity<Void> deleteCoach(@PathVariable Integer id) {
        log.info("Deleting coach with id: {}", id);
        try {
            // Check if coach exists
            if (!coachRepository.existsById(id)) {
                return ResponseEntity.notFound().build();
            }

            // Check if coach is referenced by any team
            List<Team> teamsWithCoach = teamRepository.findAll().stream()
                .filter(team -> team.getCoach() != null && team.getCoach().getId().equals(id))
                .collect(Collectors.toList());

            if (!teamsWithCoach.isEmpty()) {
                // Remove coach reference from teams
                for (Team team : teamsWithCoach) {
                    team.setCoach(null);
                    teamRepository.save(team);
                }
            }

            // Delete the coach
            coachRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error deleting coach: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    // Team operations
    @GetMapping("/teams")
    public List<TeamDto.Response> getAllTeams(
            @RequestParam(required = false) String teamName,
            @RequestParam(required = false) Integer yearCreated,
            @RequestParam(required = false) Integer coachId,
            @RequestParam(required = false) Integer playerId) {
        log.info("Fetching teams with filters - teamName: {}, yearCreated: {}, coachId: {}, playerId: {}", 
                teamName, yearCreated, coachId, playerId);
        
        return teamRepository.findAll().stream()
            .filter(team -> teamName == null || team.getTeamName().toLowerCase().contains(teamName.toLowerCase()))
            .filter(team -> yearCreated == null || team.getYearCreated().equals(yearCreated))
            .filter(team -> coachId == null || (team.getCoach() != null && team.getCoach().getId().equals(coachId)))
            .filter(team -> playerId == null || (team.getPlayer() != null && team.getPlayer().getId().equals(playerId)))
            .map(mapper::toDto)
            .collect(Collectors.toList());
    }

    @PostMapping("/teams")
    public TeamDto.Response createTeam(@RequestBody TeamDto.Request request) {
        log.info("Creating team with request: {}", request);
        
        // Create team entity
        Team team = mapper.toEntity(request);
        
        // Set coach if provided
        if (request.getCoachId() != null) {
            Coach coach = coachRepository.findById(request.getCoachId())
                .orElseThrow(() -> new IllegalArgumentException("Coach not found with id: " + request.getCoachId()));
            team.setCoach(coach);
        }
        
        // Set player if provided
        if (request.getPlayerId() != null) {
            Player player = playerRepository.findById(request.getPlayerId())
                .orElseThrow(() -> new IllegalArgumentException("Player not found with id: " + request.getPlayerId()));
            team.setPlayer(player);
        }
        
        team = teamRepository.save(team);
        return mapper.toDto(team);
    }

    @GetMapping("/teams/{id}")
    public TeamDto.Response getTeam(@PathVariable Integer id) {
        log.info("Fetching team with id: {}", id);
        return teamRepository.findById(id)
            .map(mapper::toDto)
            .orElse(null);
    }

    @PutMapping("/teams/{id}")
    public TeamDto.Response updateTeam(@PathVariable Integer id, @RequestBody TeamDto.Request request) {
        log.info("Updating team with id: {}", id);
        
        // Create new team entity
        Team team = mapper.toEntity(request);
        team.setTeamId(id);
        
        // Set coach if provided
        if (request.getCoachId() != null) {
            Coach coach = coachRepository.findById(request.getCoachId())
                .orElseThrow(() -> new IllegalArgumentException("Coach not found with id: " + request.getCoachId()));
            team.setCoach(coach);
        }
        
        // Set player if provided
        if (request.getPlayerId() != null) {
            Player player = playerRepository.findById(request.getPlayerId())
                .orElseThrow(() -> new IllegalArgumentException("Player not found with id: " + request.getPlayerId()));
            team.setPlayer(player);
        }
        
        Team updatedTeam = teamRepository.save(team);
        return mapper.toDto(updatedTeam);
    }

    @DeleteMapping("/teams/{id}")
    public ResponseEntity<Void> deleteTeam(@PathVariable Integer id) {
        log.info("Deleting team with id: {}", id);
        try {
            // Check if team exists
            if (!teamRepository.existsById(id)) {
                return ResponseEntity.notFound().build();
            }

            // Delete the team
            teamRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error deleting team: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
} 