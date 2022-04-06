package com.binchoi.springboot.web;

import com.binchoi.springboot.service.race.RaceService;
import com.binchoi.springboot.web.dto.RaceResponseDto;
import com.binchoi.springboot.web.dto.RaceSaveRequestDto;
import com.binchoi.springboot.web.dto.RaceUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class RaceApiController {

    private final RaceService raceService;

    @PostMapping("/v1/race")
    public Long save(@RequestBody RaceSaveRequestDto requestDto) {return raceService.save(requestDto);}

    @GetMapping("/v1/race/{id}")
    public RaceResponseDto findById(@PathVariable Long id) {return raceService.findById(id);}

    @PutMapping("/v1/race/{id}")
    public Long update(@PathVariable Long id, @RequestBody RaceUpdateRequestDto requestDto) {
        return raceService.update(id, requestDto);
    }

    @DeleteMapping("/v1/race/{id}")
    public Long delete(@PathVariable Long id) {
        raceService.delete(id);
        return id;
    }
}
