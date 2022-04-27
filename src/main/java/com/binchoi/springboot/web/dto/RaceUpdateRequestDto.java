package com.binchoi.springboot.web.dto;

import com.binchoi.springboot.domain.race.Race;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class RaceUpdateRequestDto {

    @NotBlank(message = "Please name your race.")
    private String raceName;

    @NotBlank(message = "Please set a wager for the race.")
    private String wager;

    private LocalDate startDate;

    private LocalDate endDate;

    @NotBlank(message = "Please state the habit you wish to build.")
    private String fstUserHabit;

    @NotBlank(message = "Please state the habit you wish to build.")
    private String sndUserHabit;

    @Builder
    public RaceUpdateRequestDto(String raceName, String wager, LocalDate startDate, LocalDate endDate,
                                String fstUserHabit, String sndUserHabit) {
        this.raceName = raceName;
        this.wager = wager;
        this.startDate = startDate;
        this.endDate = endDate;
        this.fstUserHabit = fstUserHabit;
        this.sndUserHabit = sndUserHabit;
    }

}
