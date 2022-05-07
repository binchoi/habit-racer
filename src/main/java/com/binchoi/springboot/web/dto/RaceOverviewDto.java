package com.binchoi.springboot.web.dto;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public class RaceOverviewDto {
    private Long fstUserId;
    private Long sndUserId;
    private String fstUserName;
    private String sndUserName;
    private List<PostsListResponseDto> fstUserPosts;
    private List<PostsListResponseDto> sndUserPosts;
    private Long id;
    private String raceName;
    private String wager;
    private String fstUserHabit;
    private String sndUserHabit;
    private Integer fstUserSuccessCount;
    private Integer sndUserSuccessCount;
    private Integer fstUserSuccessPercent;
    private Integer sndUserSuccessPercent;
    private List<Integer> fstUserVictory;
    private String winnerName;
    private String loserName;
    private Integer postDifference;

    public RaceOverviewDto(String fstUserName, String sndUserName,
                           List<PostsListResponseDto> fstUserPosts, List<PostsListResponseDto> sndUserPosts,
                           RaceResponseDto race, Integer daysFromStart) {
        this.fstUserId = race.getFstUserId();
        this.sndUserId = race.getSndUserId();
        this.fstUserName = fstUserName;
        this.sndUserName = sndUserName;
        this.fstUserPosts = fstUserPosts;
        this.sndUserPosts = sndUserPosts;
        this.id = race.getId();
        this.raceName = race.getRaceName();
        this.wager = race.getWager();
        this.fstUserHabit = race.getFstUserHabit();
        this.sndUserHabit = (sndUserId!=null) ? race.getSndUserHabit() : "TBD";
        this.fstUserSuccessCount = fstUserPosts.size();
        this.sndUserSuccessCount = sndUserPosts.size();
        this.fstUserSuccessPercent = fstUserSuccessCount*100 / daysFromStart;
        this.sndUserSuccessPercent = sndUserSuccessCount*100 / daysFromStart;
        this.fstUserVictory = fstUserSuccessCount>sndUserSuccessCount ? Arrays.asList(1) : null;
        this.winnerName = fstUserVictory!=null ? fstUserName : sndUserName;
        this.loserName = fstUserVictory!=null ? sndUserName : fstUserName;
        this.postDifference = fstUserSuccessCount>sndUserSuccessCount ? fstUserSuccessCount-sndUserSuccessCount : sndUserSuccessCount-fstUserSuccessCount;
    }
}
