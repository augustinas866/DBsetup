package com.example.project.dto;

import lombok.Data;
import java.time.LocalDate;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Coach data transfer object")
public class CoachDto {
    @Data
    @Schema(description = "Coach creation request", name = "CoachRequest")
    public static class Request {
        @Schema(description = "Coach's first name", example = "David", required = true)
        private String name;
        
        @Schema(description = "Coach's last name", example = "Brown", required = true)
        private String surname;
        
        @Schema(description = "Date when coach started coaching", example = "2015-01-01", required = true)
        private LocalDate coachingFrom;
        
        @Schema(description = "Coach's license ID", example = "LIC456", required = true)
        private String licenseId;
        
        @Schema(description = "Coach's personal code", example = "PC456", required = true)
        private String personalCode;
    }

    @Data
    @Schema(description = "Coach response", name = "CoachResponse")
    public static class Response {
        @Schema(description = "Coach's unique identifier")
        private Integer id;
        
        @Schema(description = "Coach's first name")
        private String name;
        
        @Schema(description = "Coach's last name")
        private String surname;
        
        @Schema(description = "Date when coach started coaching")
        private LocalDate coachingFrom;
        
        @Schema(description = "Coach's license ID")
        private String licenseId;
        
        @Schema(description = "Coach's personal code")
        private String personalCode;
    }
} 