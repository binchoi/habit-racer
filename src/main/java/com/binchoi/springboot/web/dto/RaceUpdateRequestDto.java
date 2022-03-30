package com.binchoi.springboot.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class RaceUpdateRequestDto {

    private LocalDate endDate;
    private Long sndUserId;
    private String sndUserHabit;

    @Builder
    public RaceUpdateRequestDto(LocalDate endDate, Long sndUserId, String sndUserHabit) {
        this.endDate = endDate;
        this.sndUserId = sndUserId;
        this.sndUserHabit = sndUserHabit;
    }

}
