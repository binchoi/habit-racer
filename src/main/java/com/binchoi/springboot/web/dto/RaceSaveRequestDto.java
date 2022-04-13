package com.binchoi.springboot.web.dto;

import com.binchoi.springboot.domain.race.Race;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class RaceSaveRequestDto {

    @NotBlank(message = "Please name your race.")
    private String raceName;

    @NotBlank(message = "Please set a wager for the race.")
    private String wager;

    private LocalDate startDate;

    private LocalDate endDate;

    private Long fstUserId;

    @NotBlank(message = "Please state the habit you wish to build.")
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
