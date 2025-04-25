package com.example.project.dto;

import lombok.Data;
import java.time.LocalDate;

public class CoachDto {
    @Data
    public static class Request {
        private String name;
        private String surname;
        private LocalDate coachingFrom;
        private String licenseId;
        private String personalCode;
    }

    @Data
    public static class Response {
        private Integer id;
        private String name;
        private String surname;
        private LocalDate coachingFrom;
        private String licenseId;
        private String personalCode;
    }
} 