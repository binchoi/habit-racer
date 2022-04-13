package com.binchoi.springboot.service.race;

import com.binchoi.springboot.domain.exception.CustomValidationException;
import com.binchoi.springboot.domain.race.Race;
import com.binchoi.springboot.domain.race.RaceRepository;
import com.binchoi.springboot.web.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class RaceService {
    private final RaceRepository raceRepository;

    @Transactional
    public Long save(RaceSaveRequestDto requestDto) {
        verifyStartDate(requestDto.getStartDate());
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
        verifyEndDate(entity.getStartDate(), requestDto.getEndDate());
        entity.update(requestDto.getEndDate(), requestDto.getSndUserId(), requestDto.getSndUserHabit());
        return id;
    }

    @Transactional
    public void delete(Long id) {
        Race entity = raceRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("The race does not exist. id="+ id));
        raceRepository.delete(entity);
    }

    @Transactional(readOnly = true)
    public List<RaceListResponseDto> findByUserId(Long userId) {
        return raceRepository.findByUserId(userId).stream()
                .map(RaceListResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public Long verifyRaceEligibility(Long raceId, Long sndUserId) {
        if (raceId == 0) { //set by $.ajax when raceId is not provided (consider better way)
            throw new CustomValidationException("Please enter a Race Id.", "raceId");
        }
        Race entity = raceRepository.findById(raceId)
                .orElseThrow(() -> new CustomValidationException("This race does not exist.", "raceId"));
        if (entity.getSndUserId()!=null) {
            throw new CustomValidationException("This race is fully occupied.", "raceId");
        } else if (entity.getFstUserId().equals(sndUserId)) {
            throw new CustomValidationException("You cannot race against yourself.", "raceId");
        }
        return raceId;
    }

    private void verifyStartDate(LocalDate proposedDate) {
        if (LocalDate.now().isAfter(proposedDate)) {
            throw new CustomValidationException("The start date cannot be before today.", "startDate");
        }
    }

    private void verifyEndDate(LocalDate startDate, LocalDate proposedDate) {
        if (startDate.isAfter(proposedDate)) {
            throw new CustomValidationException("The end date must be after the start date.", "endDate");
        }
    }

}
