package com.binchoi.springboot.web.dto;

import com.binchoi.springboot.domain.exception.CustomValidationException;
import com.binchoi.springboot.domain.race.Race;
import com.binchoi.springboot.domain.race.RaceRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
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

    //common user scenario
    @Test
    @WithMockUser(roles = "USER")
    public void Race_can_be_posted_updated_and_getted() throws Exception {
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

        LocalDate endRevised = end.plusMonths(1);
        Long idSec = 2L;
        String sndHabit = "To workout at least 60 minutes every day!";

        RaceUpdateRequestDto updateRequestDto = RaceUpdateRequestDto.builder()
                .endDate(endRevised)
                .sndUserId(idSec)
                .sndUserHabit(sndHabit)
                .build();

        String postUrl = "http://localhost:" + port + "/api/v1/race";

        //when
        mvc.perform(post(postUrl)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk());

        Long raceId = raceRepository.findAll().get(0).getId();
        String url = "http://localhost:" + port + "/api/v1/race/" + raceId;

        mvc.perform(put(url)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(objectMapper.writeValueAsString(updateRequestDto)))
                .andExpect(status().isOk());

        mvc.perform(get(url))
                .andExpect(status().isOk()) //then
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(raceId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fstUserId").value(id))
                .andExpect(MockMvcResultMatchers.jsonPath("$.sndUserId").value(idSec))
                .andExpect(MockMvcResultMatchers.jsonPath("$.wager").value(wager))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fstUserHabit").value(fstHabit))
                .andExpect(MockMvcResultMatchers.jsonPath("$.sndUserHabit").value(sndHabit))
                .andExpect(MockMvcResultMatchers.jsonPath("$.endDate").value(endRevised.toString()));
    }

    @Test
    @WithMockUser(roles = "USER") //no more than two people per race
    public void Full_race_cannot_be_joined() throws Exception {
        //given
        String raceName = "The epic battle of two alpha baboons";
        String wager = "7 Tons of bananas and the position of alpha baboon";
        LocalDate start = LocalDate.now();
        LocalDate end = LocalDate.now().plusMonths(1);
        Long id = 1L;
        String fstHabit = "To workout at least 10 minutes every day";
        Long idSec = 2L;
        String sndHabit = "To workout at least 60 minutes every day!";

        Long raceId = raceRepository.save(Race.builder()
                .raceName(raceName)
                .wager(wager)
                .startDate(start)
                .endDate(end)
                .fstUserId(id)
                .sndUserId(idSec)
                .fstUserHabit(fstHabit)
                .sndUserHabit(sndHabit)
                .build()).getId();

        String url = "http://localhost:"+port+"/api/v1/race/"+raceId;

        Long idSec2 = 3L;

        String eligibilityUrl = "http://localhost:"+port+"/api/v1/race/"+raceId+"/check-eligibility/"+idSec2;
        //when

        mvc.perform(get(eligibilityUrl))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof CustomValidationException));

        Race race = raceRepository.findAll().get(0);
        assertThat(race.getEndDate()).isEqualTo(end);
        assertThat(race.getSndUserId()).isEqualTo(idSec);
        assertThat(race.getSndUserHabit()).isEqualTo(sndHabit);
    }

    @Test
    @WithMockUser
    public void Race_cannot_be_joined_by_race_creator() throws Exception {
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

        String url = "http://localhost:"+port+"/api/v1/race/"+raceId+"/check-eligibility/"+id;
        //when
        mvc.perform(get(url)) //then
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof CustomValidationException));

        assertThat(raceRepository.findAll().get(0).getSndUserId()).isNull();
    }

    @Test
    @WithMockUser
    public void Nonexistent_race_cannot_be_joined() throws Exception {
        //given
        String url = "http://localhost:"+port+"/api/v1/race/"+7+"/check-eligibility/"+1;

        //when
        mvc.perform(get(url)) //then
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof CustomValidationException));
    }

    @Test
    @WithMockUser
    public void Race_cannot_be_posted_without_raceName() throws Exception {
        //given
        String wager = "7 Tons of bananas and the position of alpha baboon";
        LocalDate start = LocalDate.of(2020,2,2);
        LocalDate end = LocalDate.of(2020,3,2);
        Long id = 1L;
        String fstHabit = "To workout at least 10 minutes every day";

        RaceSaveRequestDto requestDto = RaceSaveRequestDto.builder()
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
                .content(objectMapper.writeValueAsString(requestDto))) //then
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertThat(result.getResolvedException().getMessage())
                        .contains("Please name your race."));
    }

    @Test
    @WithMockUser
    public void Race_cannot_be_posted_without_wager() throws Exception {
        //given
        String raceName = "The epic battle of two alpha baboons";
        LocalDate start = LocalDate.of(2020,2,2);
        LocalDate end = LocalDate.of(2020,3,2);
        Long id = 1L;
        String fstHabit = "To workout at least 10 minutes every day";

        RaceSaveRequestDto requestDto = RaceSaveRequestDto.builder()
                .raceName(raceName)
                .startDate(start)
                .endDate(end)
                .fstUserId(id)
                .fstUserHabit(fstHabit)
                .build();

        String url = "http://localhost:"+port+"/api/v1/race";

        //when
        mvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(requestDto))) //then
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertThat(result.getResolvedException().getMessage())
                        .contains("Please set a wager for the race."));
    }

    @Test
    @WithMockUser
    public void Race_cannot_be_posted_without_fstUserHabit() throws Exception {
        //given
        String raceName = "The epic battle of two alpha baboons";
        String wager = "7 Tons of bananas and the position of alpha baboon";
        LocalDate start = LocalDate.of(2020,2,2);
        LocalDate end = LocalDate.of(2020,3,2);
        Long id = 1L;

        RaceSaveRequestDto requestDto = RaceSaveRequestDto.builder()
                .raceName(raceName)
                .wager(wager)
                .startDate(start)
                .endDate(end)
                .fstUserId(id)
                .build();

        String url = "http://localhost:"+port+"/api/v1/race";

        //when
        mvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(requestDto))) //then
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertThat(result.getResolvedException().getMessage())
                        .contains("Please state the habit you wish to build."));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void Race_cannot_be_updated_without_sndUserHabit() throws Exception {
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

        RaceUpdateRequestDto requestDto = RaceUpdateRequestDto.builder()
                .endDate(endRevised)
                .sndUserId(idSec)
                .build();

        String url = "http://localhost:"+port+"/api/v1/race/"+raceId;

        //when
        mvc.perform(put(url)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(requestDto))) //then
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertThat(result.getResolvedException().getMessage())
                        .contains("Please state the habit you wish to build."));

        //Race entity = raceRepository.findById(raceId).get();
        //assertThat(entity.getSndUserId()).isNull();
        //assertThat(entity.getSndUserHabit()).isNull();
    }
}
