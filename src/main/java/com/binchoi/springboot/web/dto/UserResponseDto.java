package com.binchoi.springboot.web.dto;

import com.binchoi.springboot.domain.race.Race;
import com.binchoi.springboot.domain.user.User;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class UserResponseDto {
    private Long id;
    private String name;
    private String picture;


    @JsonCreator
    public UserResponseDto(User entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.picture = entity.getPicture();
    }
}
