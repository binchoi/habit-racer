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

    public RaceTimeInfoDto(RaceResponseDto raceResponseDto) {
        this.raceDuration = ChronoUnit.DAYS.between(raceResponseDto.getStartDate(), raceResponseDto.getEndDate())+1;
        this.daysFromStart = ChronoUnit.DAYS.between(raceResponseDto.getStartDate(), LocalDate.now())+1;
        this.daysUntilEnd = ChronoUnit.DAYS.between(LocalDate.now(), raceResponseDto.getEndDate());
    }
}
