package com.example.project.dto;

import lombok.Data;
import java.time.LocalDate;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Player data transfer object")
public class PlayerDto {
    @Data
    @Schema(description = "Player creation request", name = "PlayerRequest")
    public static class Request {
        @Schema(description = "Player's first name", example = "James", required = true)
        private String name;
        
        @Schema(description = "Player's last name", example = "Wilson", required = true)
        private String surname;
        
        @Schema(description = "Player's date of birth", example = "1995-05-15", required = true)
        private LocalDate dateOfBirth;
        
        @Schema(description = "Player's personal code", example = "PC789", required = true)
        private String personalCode;
    }

    @Data
    @Schema(description = "Player response", name = "PlayerResponse")
    public static class Response {
        @Schema(description = "Player's unique identifier")
        private Integer id;
        
        @Schema(description = "Player's first name")
        private String name;
        
        @Schema(description = "Player's last name")
        private String surname;
        
        @Schema(description = "Player's date of birth")
        private LocalDate dateOfBirth;
        
        @Schema(description = "Player's personal code")
        private String personalCode;
    }
} 