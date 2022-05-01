package com.binchoi.springboot.web.dto;

import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
public class UserRaceInfoDto {

    private Long userId;
    private List<RaceListResponseDto> ongoingRaceList;
    private List<RaceListResponseDto> completeRaceList;
    private Integer ongoingRaceCount;
    private Integer completeRaceCount;
    private Integer totalRaceCount;

    // consider win percentage

    public UserRaceInfoDto(Long userId, Map<Boolean, List<RaceListResponseDto>> raceMap) {
        this.userId = userId;
        this.ongoingRaceList = raceMap.get(Boolean.FALSE);
        this.completeRaceList = raceMap.get(Boolean.TRUE);
        this.ongoingRaceCount = ongoingRaceList==null ? 0 : ongoingRaceList.size();
        this.completeRaceCount = completeRaceList==null ? 0 : completeRaceList.size();
        this.totalRaceCount = ongoingRaceCount+completeRaceCount;
    }
}
