package com.binchoi.springboot.web.dto;

import com.binchoi.springboot.domain.race.Race;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class RaceResponseDto {
    private Long id;
    private String raceName;
    private String wager;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long fstUserId;
    private String fstUserHabit;
    private Long sndUserId;
    private String sndUserHabit;

    @JsonCreator
    public RaceResponseDto(Race entity) {
        this.id = entity.getId();
        this.raceName = entity.getRaceName();
        this.wager = entity.getWager();
        this.startDate = entity.getStartDate();
        this.endDate = entity.getEndDate();
        this.fstUserId = entity.getFstUserId();
        this.fstUserHabit = entity.getFstUserHabit();
        this.sndUserId = entity.getSndUserId();
        this.sndUserHabit = entity.getSndUserHabit();
    }
}
