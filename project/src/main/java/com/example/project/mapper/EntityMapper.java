package com.example.project.mapper;

import com.example.project.dto.PlayerDto;
import com.example.project.dto.CoachDto;
import com.example.project.dto.TeamDto;
import com.example.project.model.Player;
import com.example.project.model.Coach;
import com.example.project.model.Team;
import org.springframework.stereotype.Component;

@Component
public class EntityMapper {

    public Player toEntity(PlayerDto.Request dto) {
        Player player = new Player();
        player.setName(dto.getName());
        player.setSurname(dto.getSurname());
        player.setDateOfBirth(dto.getDateOfBirth());
        player.setPersonalCode(dto.getPersonalCode());
        return player;
    }

    public PlayerDto.Response toDto(Player entity) {
        PlayerDto.Response dto = new PlayerDto.Response();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setSurname(entity.getSurname());
        dto.setDateOfBirth(entity.getDateOfBirth());
        dto.setPersonalCode(entity.getPersonalCode());
        return dto;
    }

    public Coach toEntity(CoachDto.Request dto) {
        Coach coach = new Coach();
        coach.setName(dto.getName());
        coach.setSurname(dto.getSurname());
        coach.setCoachingFrom(dto.getCoachingFrom());
        coach.setLicenseId(dto.getLicenseId());
        coach.setPersonalCode(dto.getPersonalCode());
        return coach;
    }

    public CoachDto.Response toDto(Coach entity) {
        CoachDto.Response dto = new CoachDto.Response();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setSurname(entity.getSurname());
        dto.setCoachingFrom(entity.getCoachingFrom());
        dto.setLicenseId(entity.getLicenseId());
        dto.setPersonalCode(entity.getPersonalCode());
        return dto;
    }

    public Team toEntity(TeamDto.Request dto) {
        Team team = new Team();
        // These will need to be set by the service layer after fetching the actual entities
        team.setTeamName(dto.getTeamName());
        team.setYearCreated(dto.getYearCreated());
        return team;
    }

    public TeamDto.Response toDto(Team entity) {
        TeamDto.Response dto = new TeamDto.Response();
        dto.setTeamId(entity.getTeamId());
        dto.setTeamName(entity.getTeamName());
        dto.setYearCreated(entity.getYearCreated());
        if (entity.getCoach() != null) {
            dto.setCoach(toDto(entity.getCoach()));
        }
        if (entity.getPlayer() != null) {
            dto.setPlayer(toDto(entity.getPlayer()));
        }
        return dto;
    }
} 