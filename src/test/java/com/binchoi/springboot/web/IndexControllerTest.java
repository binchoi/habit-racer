package com.binchoi.springboot.web;

import com.binchoi.springboot.domain.race.RaceRepository;
import com.binchoi.springboot.web.dto.RaceSaveRequestDto;
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
public class IndexControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RaceRepository raceRepository;

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
    public void main_page_loading() {
        //when
        String body = this.restTemplate.getForObject("/", String.class);

        //then
        assertThat(body).contains("HabitRacer");
    }
//
//    @Test
//    @WithMockUser(roles = "USER")
//    public void Race_overview_page_loading() throws Exception {
//        //given
//        String raceName = "The epic battle of two alpha baboons";
//        String wager = "7 Tons of bananas and the position of alpha baboon";
//        LocalDate start = LocalDate.now();
//        LocalDate end = LocalDate.now().plusMonths(1);
//        Long id = 1L;
//        String fstHabit = "To workout at least 10 minutes every day";
//
//
//        RaceSaveRequestDto requestDto = RaceSaveRequestDto.builder()
//                .raceName(raceName)
//                .wager(wager)
//                .startDate(start)
//                .endDate(end)
//                .fstUserId(id)
//                .fstUserHabit(fstHabit)
//                .build();
//
//        String url = "http://localhost:"+port+"/api/v1/race";
//        String raceId = mvc.perform(post(url)
//                .contentType(MediaType.APPLICATION_JSON_UTF8)
//                .content(objectMapper.writeValueAsString(requestDto)))
////                .with(oauth2Login()))
//                .andExpect(status().isOk())
//                .andReturn().getResponse().getContentAsString();
//
//        //when
//        String urlRaceOverview = "http://localhost:"+port+"/race/"+raceId;
//        mvc.perform(get(urlRaceOverview))
//                .andExpect(status().isOk())
//                .andDo(print());
//    }


}