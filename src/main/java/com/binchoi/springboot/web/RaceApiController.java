package com.binchoi.springboot.web;

import com.binchoi.springboot.service.race.RaceService;
import com.binchoi.springboot.web.dto.RaceJoinRequestDto;
import com.binchoi.springboot.web.dto.RaceResponseDto;
import com.binchoi.springboot.web.dto.RaceSaveRequestDto;
import com.binchoi.springboot.web.dto.RaceUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class RaceApiController {

    private final RaceService raceService;

    @PostMapping("/v1/race")
    public Long save(@Valid @RequestBody RaceSaveRequestDto requestDto) {
        return raceService.save(requestDto);
    }

    @PutMapping("/v1/race/{id}")
    public Long update(@PathVariable Long id, @Valid @RequestBody RaceUpdateRequestDto requestDto) {
        return raceService.update(id, requestDto);
    }

    @PutMapping("/v1/race/join/{id}")
    public Long join(@PathVariable Long id, @Valid @RequestBody RaceJoinRequestDto requestDto) {
        return raceService.join(id, requestDto);
    }

    @GetMapping("/v1/race/{id}")
    public RaceResponseDto findById(@PathVariable Long id) {
        return raceService.findById(id);
    }


    @GetMapping("/v1/race/{raceId}/joinable/{sndUserId}")
    public Long verifyRaceEligibility(@PathVariable Long raceId, @PathVariable Long sndUserId) {
        return raceService.verifyRaceEligibility(raceId, sndUserId);
    }

    @DeleteMapping("/v1/race/{id}")
    public Long delete(@PathVariable Long id) {
        raceService.delete(id);
        return id;
    }
}
