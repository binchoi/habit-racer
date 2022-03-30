package com.binchoi.springboot.web.dto;

import com.binchoi.springboot.domain.race.Race;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class RaceSaveRequestDto {
    private String raceName;
    private String wager;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long fstUserId;
    private String fstUserHabit;

    @Builder
    public RaceSaveRequestDto(String raceName, String wager, LocalDate startDate, LocalDate endDate, Long fstUserId, String fstUserHabit) {
        this.raceName = raceName;
        this.wager = wager;
        this.startDate = startDate;
        this.endDate = endDate;
        this.fstUserId = fstUserId;
        this.fstUserHabit = fstUserHabit;
    }

    public Race toEntity() {
        return Race.builder()
                .raceName(raceName)
                .wager(wager)
                .startDate(startDate)
                .endDate(endDate)
                .fstUserId(fstUserId)
                .fstUserHabit(fstUserHabit)
                .build();
    }
}
