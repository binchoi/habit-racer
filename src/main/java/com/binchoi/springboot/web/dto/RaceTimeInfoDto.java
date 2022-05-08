package com.binchoi.springboot.web.dto;

import com.binchoi.springboot.domain.race.Race;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Getter
public class RaceTimeInfoDto {
    private Long raceDuration;
    private Long daysFromStart;
    private Long daysUntilEnd;
    private Boolean isOver;

    public RaceTimeInfoDto(RaceResponseDto raceResponseDto) {
        this.raceDuration = ChronoUnit.DAYS.between(raceResponseDto.getStartDate(), raceResponseDto.getEndDate())+1;
        this.daysFromStart = Math.min(this.raceDuration, Math.max(1, ChronoUnit.DAYS.between(raceResponseDto.getStartDate(), LocalDate.now())+1));
        this.daysUntilEnd = Math.max(0,ChronoUnit.DAYS.between(LocalDate.now(), raceResponseDto.getEndDate()));
        this.isOver = (this.daysUntilEnd==0);
    }
}
