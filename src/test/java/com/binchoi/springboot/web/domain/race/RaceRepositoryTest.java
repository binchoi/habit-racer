package com.binchoi.springboot.web.domain.race;

import com.binchoi.springboot.domain.posts.Posts;
import com.binchoi.springboot.domain.race.Race;
import com.binchoi.springboot.domain.race.RaceRepository;
import com.binchoi.springboot.web.dto.RaceSaveRequestDto;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RaceRepositoryTest {

    @Autowired
    RaceRepository raceRepository;

    @After
    public void cleanup() {raceRepository.deleteAll();}

    @Test
    public void get_Race() {
        //given
        String raceName = "The epic battle of two alpha baboons";
        String wager = "7 Tons of bananas and the position of alpha baboon";
        LocalDate start = LocalDate.now();
        LocalDate end = LocalDate.now().plusMonths(1);
        Long id = 1L;
        String fstHabit = "To workout at least 10 minutes every day";

        raceRepository.save(Race.builder()
                .raceName(raceName)
                .wager(wager)
                .startDate(start)
                .endDate(end)
                .fstUserId(id)
                .fstUserHabit(fstHabit)
                .build());

        //when
        List<Race> raceList = raceRepository.findAll();

        //then
        Race race = raceList.get(0);

        assertThat(race.getRaceName()).isEqualTo(raceName);
        assertThat(race.getWager()).isEqualTo(wager);
        assertThat(race.getStartDate()).isEqualTo(start);
        assertThat(race.getEndDate()).isEqualTo(end);
        assertThat(race.getFstUserId()).isEqualTo(id);
        assertThat(race.getFstUserHabit()).isEqualTo(fstHabit);
    }

    @Test
    public void BaseTimeEntity_Test2() {
        //given
        LocalDateTime now = LocalDateTime.of(2029,6,4,0,0,0);

        String raceName = "The epic battle of two alpha baboons";
        String wager = "7 Tons of bananas and the position of alpha baboon";
        LocalDate start = LocalDate.now();
        LocalDate end = LocalDate.now().plusMonths(1);
        Long id = 1L;
        String fstHabit = "To workout at least 10 minutes every day";

        raceRepository.save(Race.builder()
                .raceName(raceName)
                .wager(wager)
                .startDate(start)
                .endDate(end)
                .fstUserId(id)
                .fstUserHabit(fstHabit)
                .build());

        //when
        List<Race> raceList = raceRepository.findAll();

        //then
        Race race = raceList.get(0);
        assertThat(race.getCreatedDate().isAfter(now));
        assertThat(race.getModifiedDate().isAfter(now));
    }
}
