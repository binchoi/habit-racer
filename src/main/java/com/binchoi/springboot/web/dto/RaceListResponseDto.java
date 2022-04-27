package com.binchoi.springboot.web.dto;

import com.binchoi.springboot.domain.race.Race;
import lombok.Getter;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Getter
public class RaceListResponseDto {
    private Long id;
    private String raceName;
    private String wager;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long fstUserId;
    private String fstUserHabit;
    private Long sndUserId;
    private String sndUserHabit;
    private Long daysUntilEnd;
    private Boolean isComplete;
    private String competitorName;

    public RaceListResponseDto(Race entity, String competitorName) {
        this.id = entity.getId();
        this.raceName = entity.getRaceName();
        this.wager = entity.getWager();
        this.startDate = entity.getStartDate();
        this.endDate = entity.getEndDate();
        this.fstUserId = entity.getFstUserId();
        this.fstUserHabit = entity.getFstUserHabit();
        this.sndUserId = entity.getSndUserId();
        this.sndUserHabit = entity.getSndUserHabit();
        this.daysUntilEnd = ChronoUnit.DAYS.between(LocalDate.now(), entity.getEndDate());
        this.isComplete = this.daysUntilEnd<0;
        this.competitorName = competitorName;
    }
}
