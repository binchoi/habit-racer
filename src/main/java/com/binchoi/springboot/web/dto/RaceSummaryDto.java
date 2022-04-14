//package com.binchoi.springboot.web.dto;
//
//import com.binchoi.springboot.domain.race.Race;
//import lombok.Builder;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//
//import java.time.LocalDate;
//
//@Getter
//@NoArgsConstructor
//public class RaceSummaryDto {
//    private Long id;
//    private String raceName;
//    private String wager;
//    private LocalDate startDate;
//    private LocalDate endDate;
//    private Long fstUserId;
//    private String fstUserHabit;
//    private String fstUserName; // needs user repository
//    private Long sndUserId;
//    private String sndUserHabit;
//    private String sndUserName;
//    private Long daysPast;
//    private Long daysRemaining;
//    private Long fstUserSuccessCount;
//    private Long sndUserSuccessCount;
//    private String fstUserVictoryLikelihood;
//    private String sndUserVictoryLikelihood;
//
//
//
//    @Builder
//    public RaceSummaryDto(Long id, String raceName, String wager, LocalDate startDate, LocalDate endDate,
//                          Long fstUserId, String fstUserHabit, String fstUserName, Long sndUserId, String sndUserHabit,
//                          Long daysPast, String sndUserName, Long daysRemaining, Long fstUserSuccessCount,
//                          Long sndUserSuccessCount, String fstUserVictoryLikelihood, String sndUserVictoryLikelihood) {
//        this.id = id;
//        this.raceName = raceName;
//        this.wager = wager;
//        this.startDate = startDate;
//        this.endDate = endDate;
//        this.fstUserId = fstUserId;
//        this.fstUserHabit = fstUserHabit;
//        this.fstUserName = fstUserName;
//        this.sndUserId = sndUserId;
//        this.sndUserHabit = sndUserHabit;
//        this.sndUserName = sndUserName;
//        this.daysPast = daysPast;
//        this.daysRemaining = daysRemaining;
//        this.fstUserSuccessCount = fstUserSuccessCount;
//        this.sndUserSuccessCount = sndUserSuccessCount;
//        this.fstUserVictoryLikelihood = fstUserVictoryLikelihood;
//        this.sndUserVictoryLikelihood = sndUserVictoryLikelihood;
//    }
//
//    public static RaceSummaryDto of(Race entity) {
//        RaceSummaryDto raceSummaryDto = RaceSummaryDto.builder()
//                .id(entity.getId())
//                .raceName(entity.getRaceName())
//                .wager(entity.getWager())
//                .startDate(entity.getStartDate())
//                .endDate(entity.getEndDate())
//                .fstUserId(entity.getFstUserId())
//                .fstUserHabit(entity.getFstUserHabit())
//                .fstUserName(userService.findById(fstUserId).getName()))
//    }
//}
