package com.binchoi.springboot.service.race;

import com.binchoi.springboot.domain.race.Race;
import com.binchoi.springboot.domain.race.RaceRepository;
import com.binchoi.springboot.web.dto.RaceResponseDto;
import com.binchoi.springboot.web.dto.RaceSaveRequestDto;
import com.binchoi.springboot.web.dto.RaceUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class RaceService {
    private final RaceRepository raceRepository;

    @Transactional
    public Long save(RaceSaveRequestDto requestDto) {
        return raceRepository.save(requestDto.toEntity()).getId();
    }

    @Transactional
    public RaceResponseDto findById(Long id) {
        Race entity = raceRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("The race does not exist. id="+ id));
        return new RaceResponseDto(entity);
    }

    @Transactional
    public Long update(Long id, RaceUpdateRequestDto requestDto) {
        Race entity = raceRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("The race does not exist. id="+ id));
        entity.update(requestDto.getEndDate(), requestDto.getSndUserId(), requestDto.getSndUserHabit());
        return id;
    }

    @Transactional
    public void delete(Long id) {
        Race entity = raceRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("The race does not exist. id="+ id));
        raceRepository.delete(entity);
    }

}
