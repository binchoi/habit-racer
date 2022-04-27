package com.binchoi.springboot.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class RaceJoinRequestDto {

    private LocalDate endDate;
    private Long sndUserId;

    @NotBlank(message = "Please state the habit you wish to build.")
    private String sndUserHabit;

    @Builder
    public RaceJoinRequestDto(LocalDate endDate, Long sndUserId, String sndUserHabit) {
        this.endDate = endDate;
        this.sndUserId = sndUserId;
        this.sndUserHabit = sndUserHabit;
    }

}
