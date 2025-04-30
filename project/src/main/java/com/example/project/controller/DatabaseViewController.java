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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Sports Team Management", description = "API for managing sports teams, players, and coaches")
public class DatabaseViewController {
    
    private final PlayerRepository playerRepository;
    private final CoachRepository coachRepository;
    private final TeamRepository teamRepository;
    private final EntityMapper mapper;
    
    // Player operations
    @Operation(summary = "Get all players", description = "Retrieve a list of all players with optional filtering")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of players"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/players")
    public List<PlayerDto.Response> getAllPlayers(
            @Parameter(description = "Filter by player name") @RequestParam(required = false) String name,
            @Parameter(description = "Filter by player surname") @RequestParam(required = false) String surname,
            @Parameter(description = "Filter by player personal code") @RequestParam(required = false) String personalCode) {
        log.info("Fetching players with filters - name: {}, surname: {}, personalCode: {}", name, surname, personalCode);
        
        return playerRepository.findAll().stream()
            .filter(player -> name == null || player.getName().toLowerCase().contains(name.toLowerCase()))
            .filter(player -> surname == null || player.getSurname().toLowerCase().contains(surname.toLowerCase()))
            .filter(player -> personalCode == null || player.getPersonalCode().equals(personalCode))
            .map(mapper::toDto)
            .collect(Collectors.toList());
    }

    @Operation(summary = "Create a new player", description = "Create a new player with the provided details")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully created the player"),
        @ApiResponse(responseCode = "400", description = "Invalid request - missing required fields"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/players")
    public ResponseEntity<PlayerDto.Response> createPlayer(
            @Parameter(description = "Player creation request", required = true)
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Player creation request",
                required = true,
                content = @io.swagger.v3.oas.annotations.media.Content(
                    schema = @Schema(implementation = PlayerDto.Request.class)
                )
            )
            @RequestBody PlayerDto.Request request) {
        log.info("Creating player: {}", request);
        try {
            // Validate required fields
            if (request.getName() == null || request.getSurname() == null || 
                request.getDateOfBirth() == null || request.getPersonalCode() == null) {
                return ResponseEntity.badRequest().build();
            }

            Player player = mapper.toEntity(request);
            player = playerRepository.save(player);
            return ResponseEntity.ok(mapper.toDto(player));
        } catch (Exception e) {
            log.error("Error creating player: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(summary = "Get a player by ID", description = "Retrieve a player by their ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved the player"),
        @ApiResponse(responseCode = "404", description = "Player not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/players/{id}")
    public ResponseEntity<PlayerDto.Response> getPlayer(@Parameter(description = "Player ID") @PathVariable Integer id) {
        log.info("Fetching player with id: {}", id);
        return playerRepository.findById(id)
            .map(player -> ResponseEntity.ok(mapper.toDto(player)))
            .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Update a player", description = "Update an existing player's details")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully updated the player"),
        @ApiResponse(responseCode = "404", description = "Player not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/players/{id}")
    public ResponseEntity<PlayerDto.Response> updatePlayer(
            @Parameter(description = "Player ID") @PathVariable Integer id,
            @RequestBody PlayerDto.Request request) {
        log.info("Updating player with id: {}", id);
        return playerRepository.findById(id)
            .map(existingPlayer -> {
                Player player = mapper.toEntity(request);
                player.setId(id);
                Player updatedPlayer = playerRepository.save(player);
                return ResponseEntity.ok(mapper.toDto(updatedPlayer));
            })
            .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Delete a player", description = "Delete a player by their ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully deleted the player"),
        @ApiResponse(responseCode = "404", description = "Player not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/players/{id}")
    public ResponseEntity<Void> deletePlayer(@Parameter(description = "Player ID") @PathVariable Integer id) {
        log.info("Deleting player with id: {}", id);
        try {
            if (!playerRepository.existsById(id)) {
                return ResponseEntity.notFound().build();
            }

            List<Team> teamsWithPlayer = teamRepository.findAll().stream()
                .filter(team -> team.getPlayers().stream()
                    .anyMatch(player -> player.getId().equals(id)))
                .collect(Collectors.toList());

            if (!teamsWithPlayer.isEmpty()) {
                for (Team team : teamsWithPlayer) {
                    team.getPlayers().removeIf(player -> player.getId().equals(id));
                    teamRepository.save(team);
                }
            }

            playerRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error deleting player: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    // Coach operations
    @Operation(summary = "Get all coaches", description = "Retrieve a list of all coaches with optional filtering")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of coaches"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/coaches")
    public List<CoachDto.Response> getAllCoaches(
            @Parameter(description = "Filter by coach name") @RequestParam(required = false) String name,
            @Parameter(description = "Filter by coach surname") @RequestParam(required = false) String surname,
            @Parameter(description = "Filter by coach license ID") @RequestParam(required = false) String licenseId,
            @Parameter(description = "Filter by coach personal code") @RequestParam(required = false) String personalCode) {
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

    @Operation(summary = "Create a new coach", description = "Create a new coach with the provided details")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully created the coach"),
        @ApiResponse(responseCode = "400", description = "Invalid request - missing required fields"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/coaches")
    public ResponseEntity<CoachDto.Response> createCoach(
            @Parameter(description = "Coach creation request", required = true)
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Coach creation request",
                required = true,
                content = @io.swagger.v3.oas.annotations.media.Content(
                    schema = @Schema(implementation = CoachDto.Request.class)
                )
            )
            @RequestBody CoachDto.Request request) {
        log.info("Creating coach: {}", request);
        try {
            // Validate required fields
            if (request.getName() == null || request.getSurname() == null || 
                request.getCoachingFrom() == null || request.getLicenseId() == null || 
                request.getPersonalCode() == null) {
                return ResponseEntity.badRequest().build();
            }

            Coach coach = mapper.toEntity(request);
            coach = coachRepository.save(coach);
            return ResponseEntity.ok(mapper.toDto(coach));
        } catch (Exception e) {
            log.error("Error creating coach: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(summary = "Get a coach by ID", description = "Retrieve a coach by their ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved the coach"),
        @ApiResponse(responseCode = "404", description = "Coach not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/coaches/{id}")
    public ResponseEntity<CoachDto.Response> getCoach(@Parameter(description = "Coach ID") @PathVariable Integer id) {
        log.info("Fetching coach with id: {}", id);
        return coachRepository.findById(id)
            .map(coach -> ResponseEntity.ok(mapper.toDto(coach)))
            .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Update a coach", description = "Update an existing coach's details")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully updated the coach"),
        @ApiResponse(responseCode = "404", description = "Coach not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/coaches/{id}")
    public ResponseEntity<CoachDto.Response> updateCoach(
            @Parameter(description = "Coach ID") @PathVariable Integer id,
            @RequestBody CoachDto.Request request) {
        log.info("Updating coach with id: {}", id);
        return coachRepository.findById(id)
            .map(existingCoach -> {
                Coach coach = mapper.toEntity(request);
                coach.setId(id);
                Coach updatedCoach = coachRepository.save(coach);
                return ResponseEntity.ok(mapper.toDto(updatedCoach));
            })
            .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Delete a coach", description = "Delete a coach by their ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully deleted the coach"),
        @ApiResponse(responseCode = "404", description = "Coach not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/coaches/{id}")
    public ResponseEntity<Void> deleteCoach(@Parameter(description = "Coach ID") @PathVariable Integer id) {
        log.info("Deleting coach with id: {}", id);
        try {
            if (!coachRepository.existsById(id)) {
                return ResponseEntity.notFound().build();
            }

            List<Team> teamsWithCoach = teamRepository.findAll().stream()
                .filter(team -> team.getCoach() != null && team.getCoach().getId().equals(id))
                .collect(Collectors.toList());

            if (!teamsWithCoach.isEmpty()) {
                for (Team team : teamsWithCoach) {
                    team.setCoach(null);
                    teamRepository.save(team);
                }
            }

            coachRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error deleting coach: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    // Team operations
    @Operation(summary = "Get all teams", description = "Retrieve a list of all teams with optional filtering")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of teams"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/teams")
    public List<TeamDto.Response> getAllTeams(
            @Parameter(description = "Filter by team name") @RequestParam(required = false) String teamName,
            @Parameter(description = "Filter by year created") @RequestParam(required = false) Integer yearCreated,
            @Parameter(description = "Filter by coach ID") @RequestParam(required = false) Integer coachId,
            @Parameter(description = "Filter by player ID") @RequestParam(required = false) Integer playerId) {
        log.info("Fetching teams with filters - teamName: {}, yearCreated: {}, coachId: {}, playerId: {}", 
                teamName, yearCreated, coachId, playerId);
        
        return teamRepository.findAll().stream()
            .filter(team -> teamName == null || team.getTeamName().toLowerCase().contains(teamName.toLowerCase()))
            .filter(team -> yearCreated == null || team.getYearCreated().equals(yearCreated))
            .filter(team -> coachId == null || (team.getCoach() != null && team.getCoach().getId().equals(coachId)))
            .filter(team -> playerId == null || team.getPlayers().stream()
                .anyMatch(player -> player.getId().equals(playerId)))
            .map(mapper::toDto)
            .collect(Collectors.toList());
    }

    @Operation(summary = "Create a new team", description = "Create a new team with the provided details")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully created the team"),
        @ApiResponse(responseCode = "400", description = "Invalid request - missing required fields"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/teams")
    public ResponseEntity<TeamDto.Response> createTeam(@RequestBody TeamDto.Request request) {
        log.info("Creating team with request: {}", request);
        try {
            // Validate required fields
            if (request.getTeamName() == null || request.getYearCreated() == null) {
                return ResponseEntity.badRequest().build();
            }
            
            Team team = mapper.toEntity(request);
            
            if (request.getCoachId() != null) {
                Coach coach = coachRepository.findById(request.getCoachId())
                    .orElseThrow(() -> new IllegalArgumentException("Coach not found with id: " + request.getCoachId()));
                team.setCoach(coach);
            }
            
            if (request.getPlayerIds() != null && !request.getPlayerIds().isEmpty()) {
                Set<Player> players = request.getPlayerIds().stream()
                    .map(id -> playerRepository.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("Player not found with id: " + id)))
                    .collect(Collectors.toSet());
                team.getPlayers().addAll(players);
            }
            
            team = teamRepository.save(team);
            return ResponseEntity.ok(mapper.toDto(team));
        } catch (IllegalArgumentException e) {
            log.error("Invalid request: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Error creating team: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(summary = "Get a team by ID", description = "Retrieve a team by their ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved the team"),
        @ApiResponse(responseCode = "404", description = "Team not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/teams/{id}")
    public ResponseEntity<TeamDto.Response> getTeam(@Parameter(description = "Team ID") @PathVariable Integer id) {
        log.info("Fetching team with id: {}", id);
        return teamRepository.findById(id)
            .map(team -> ResponseEntity.ok(mapper.toDto(team)))
            .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Update a team", description = "Update an existing team's details")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully updated the team"),
        @ApiResponse(responseCode = "404", description = "Team not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/teams/{id}")
    public ResponseEntity<TeamDto.Response> updateTeam(
            @Parameter(description = "Team ID") @PathVariable Integer id,
            @RequestBody TeamDto.Request request) {
        log.info("Updating team with id: {}", id);
        return teamRepository.findById(id)
            .map(existingTeam -> {
                Team team = mapper.toEntity(request);
                team.setTeamId(id);
                
                if (request.getCoachId() != null) {
                    Coach coach = coachRepository.findById(request.getCoachId())
                        .orElseThrow(() -> new IllegalArgumentException("Coach not found with id: " + request.getCoachId()));
                    team.setCoach(coach);
                }
                
                if (request.getPlayerIds() != null) {
                    Set<Player> players = request.getPlayerIds().stream()
                        .map(playerId -> playerRepository.findById(playerId)
                            .orElseThrow(() -> new IllegalArgumentException("Player not found with id: " + playerId)))
                        .collect(Collectors.toSet());
                    team.setPlayers(players);
                }
                
                Team updatedTeam = teamRepository.save(team);
                return ResponseEntity.ok(mapper.toDto(updatedTeam));
            })
            .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Delete a team", description = "Delete a team by their ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully deleted the team"),
        @ApiResponse(responseCode = "404", description = "Team not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/teams/{id}")
    public ResponseEntity<Void> deleteTeam(@Parameter(description = "Team ID") @PathVariable Integer id) {
        log.info("Deleting team with id: {}", id);
        try {
            if (!teamRepository.existsById(id)) {
                return ResponseEntity.notFound().build();
            }

            teamRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error deleting team: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    // Add new endpoints for managing players in a team
    @PostMapping("/teams/{teamId}/players/{playerId}")
    public ResponseEntity<TeamDto.Response> addPlayerToTeam(
            @PathVariable Integer teamId,
            @PathVariable Integer playerId) {
        try {
            Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team not found"));
            Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new IllegalArgumentException("Player not found"));
            
            team.addPlayer(player);
            team = teamRepository.save(team);
            return ResponseEntity.ok(mapper.toDto(team));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/teams/{teamId}/players/{playerId}")
    public ResponseEntity<TeamDto.Response> removePlayerFromTeam(
            @PathVariable Integer teamId,
            @PathVariable Integer playerId) {
        try {
            Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team not found"));
            Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new IllegalArgumentException("Player not found"));
            
            team.removePlayer(player);
            team = teamRepository.save(team);
            return ResponseEntity.ok(mapper.toDto(team));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
} 