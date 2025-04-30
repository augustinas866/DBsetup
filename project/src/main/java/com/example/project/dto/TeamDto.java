package com.example.project.dto;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Set;

@Schema(description = "Team data transfer object")
public class TeamDto {

    @Data
    @Schema(description = "Team creation request", name = "TeamRequest")
    public static class Request {
        @Schema(description = "ID of the team's coach", example = "1")
        private Integer coachId;
        
        @Schema(description = "IDs of the team's players", example = "[1, 2, 3]")
        private Set<Integer> playerIds;
        
        @Schema(description = "Name of the team", example = "Eagles", required = true)
        private String teamName;
        
        @Schema(description = "Year when the team was created", example = "2024", required = true)
        private Integer yearCreated;
    }

    @Data
    @Schema(description = "Team response", name = "TeamResponse")
    public static class Response {
        @Schema(description = "Team's unique identifier")
        private Integer teamId;
        
        @Schema(description = "Team's coach information")
        private CoachDto.Response coach;
        
        @Schema(description = "Team's players information")
        private Set<PlayerDto.Response> players;
        
        @Schema(description = "Name of the team")
        private String teamName;
        
        @Schema(description = "Year when the team was created")
        private Integer yearCreated;
    }
} 