package com.example.project.dto;

import lombok.Data;

public class TeamDto {

    @Data
    public static class Request {
        private Integer coachId;
        private Integer playerId;
        private String teamName;
        private Integer yearCreated;
    }

    @Data
    public static class Response {
        private Integer teamId;
        private CoachDto.Response coach;
        private PlayerDto.Response player;
        private String teamName;
        private Integer yearCreated;
    }
} 