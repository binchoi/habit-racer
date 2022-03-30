package com.binchoi.springboot.web.dto;

import com.binchoi.springboot.domain.race.Race;
import com.binchoi.springboot.domain.race.RaceRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RaceApiControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private RaceRepository raceRepository;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mvc;

    @Before
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @After
    public void tearDown() throws Exception {
        raceRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles = "USER")
    public void Race_can_be_posted() throws Exception {
        //given
        String raceName = "The epic battle of two alpha baboons";
        String wager = "7 Tons of bananas and the position of alpha baboon";
        LocalDate start = LocalDate.now();
        LocalDate end = LocalDate.now().plusMonths(1);
        Long id = 1L;
        String fstHabit = "To workout at least 10 minutes every day";


        RaceSaveRequestDto requestDto = RaceSaveRequestDto.builder()
                .raceName(raceName)
                .wager(wager)
                .startDate(start)
                .endDate(end)
                .fstUserId(id)
                .fstUserHabit(fstHabit)
                .build();

        String url = "http://localhost:"+port+"/api/v1/race";

        //when
        mvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk());

        //then
        Race race = raceRepository.findAll().get(0);
        assertThat(race.getRaceName()).isEqualTo(raceName);
        assertThat(race.getWager()).isEqualTo(wager);
        assertThat(race.getStartDate()).isEqualTo(start);
        assertThat(race.getEndDate()).isEqualTo(end);
        assertThat(race.getFstUserId()).isEqualTo(id);
    }

    @Test
    @WithMockUser(roles = "USER")
    public void Race_can_be_getted() throws Exception {
        //given
        String raceName = "The epic battle of two alpha baboons";
        String wager = "7 Tons of bananas and the position of alpha baboon";
        LocalDate start = LocalDate.now();
        LocalDate end = LocalDate.now().plusMonths(1);
        Long id = 1L;
        String fstHabit = "To workout at least 10 minutes every day";


        Long raceId = raceRepository.save(Race.builder()
                .raceName(raceName)
                .wager(wager)
                .startDate(start)
                .endDate(end)
                .fstUserId(id)
                .fstUserHabit(fstHabit)
                .build()).getId();

        String url = "http://localhost:"+port+"/api/v1/race/"+raceId;

        //when
        mvc.perform(get(url)) //then
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(raceId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fstUserId").value(id))
                .andExpect(MockMvcResultMatchers.jsonPath("$.wager").value(wager))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fstUserHabit").value(fstHabit));
//                .andDo(print());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void Race_can_be_updated() throws Exception {
        //given
        String raceName = "The epic battle of two alpha baboons";
        String wager = "7 Tons of bananas and the position of alpha baboon";
        LocalDate start = LocalDate.now();
        LocalDate end = LocalDate.now().plusMonths(1);
        Long id = 1L;
        String fstHabit = "To workout at least 10 minutes every day";

        Long raceId = raceRepository.save(Race.builder()
                .raceName(raceName)
                .wager(wager)
                .startDate(start)
                .endDate(end)
                .fstUserId(id)
                .fstUserHabit(fstHabit)
                .build()).getId();

        LocalDate endRevised = end.plusMonths(1);
        Long idSec = 2L;
        String sndHabit = "To workout at least 60 minutes every day!";

        RaceUpdateRequestDto requestDto = RaceUpdateRequestDto.builder()
                .endDate(endRevised)
                .sndUserId(idSec)
                .sndUserHabit(sndHabit)
                .build();

        String url = "http://localhost:"+port+"/api/v1/race/"+raceId;

        //when
        mvc.perform(put(url)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk());
//                .andDo(print());

        //then
        Race race = raceRepository.findAll().get(0);
        assertThat(race.getEndDate()).isEqualTo(endRevised);
        assertThat(race.getSndUserId()).isEqualTo(idSec);
        assertThat(race.getSndUserHabit()).isEqualTo(sndHabit);

    }

    @Test
    @WithMockUser(roles = "USER")
    public void Race_can_be_deleted() throws Exception {
        //given
        String raceName = "The epic battle of two alpha baboons";
        String wager = "7 Tons of bananas and the position of alpha baboon";
        LocalDate start = LocalDate.now();
        LocalDate end = LocalDate.now().plusMonths(1);
        Long id = 1L;
        String fstHabit = "To workout at least 10 minutes every day";

        Long raceId = raceRepository.save(Race.builder()
                .raceName(raceName)
                .wager(wager)
                .startDate(start)
                .endDate(end)
                .fstUserId(id)
                .fstUserHabit(fstHabit)
                .build()).getId();

        String url = "http://localhost:"+port+"/api/v1/race/"+raceId;

        //when
        mvc.perform(delete(url))
                .andExpect(status().isOk())
                .andDo(print());

        //then
        assertThat(raceRepository.findAll().isEmpty());

    }
}
