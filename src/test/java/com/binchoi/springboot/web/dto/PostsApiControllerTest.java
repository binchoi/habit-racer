package com.binchoi.springboot.web.dto;

import com.binchoi.springboot.domain.posts.Posts;
import com.binchoi.springboot.domain.posts.PostsRepository;

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
import org.springframework.http.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PostsApiControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PostsRepository postsRepository;

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
        postsRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles = "USER")
    public void Posts_can_be_posted() throws Exception {
        //given
        String raceName = "The epic battle of two alpha baboons";
        String wager = "7 Tons of bananas and the position of alpha baboon";
        LocalDate start = LocalDate.now();
        LocalDate end = LocalDate.now().plusMonths(1);
        Long userId = 1L;
        String fstHabit = "To workout at least 10 minutes every day";


        Long raceId = raceRepository.save(Race.builder()
                .raceName(raceName)
                .wager(wager)
                .startDate(start)
                .endDate(end)
                .fstUserId(userId)
                .fstUserHabit(fstHabit)
                .build()).getId();

        Boolean isCompleted = true;
        String comment = "take that loser / comment";
        PostsSaveRequestDto requestDto = PostsSaveRequestDto.builder()
                .date(start)
                .userId(userId)
                .raceId(raceId)
                .isCompleted(isCompleted)
                .comment(comment)
                .build();

        String url = "http://localhost:" + port + "/api/v1/posts";

        //when
        mvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk());
        //ResponseEntity<Long> responseEntity = restTemplate.postForEntity(url, requestDto, Long.class);

        //then
        //assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        //assertThat(responseEntity.getBody()).isGreaterThan(0L);

        List<Posts> all = postsRepository.findAll();
        assertThat(all.get(0).getDate()).isEqualTo(start);
        assertThat(all.get(0).getIsCompleted()).isEqualTo(isCompleted);
        assertThat(all.get(0).getUserId()).isEqualTo(userId);
        assertThat(all.get(0).getRaceId()).isEqualTo(raceId);
        assertThat(all.get(0).getComment()).isEqualTo(comment);
    }

    @Test
    @WithMockUser(roles = "USER")
    public void Posts_can_be_getted() throws Exception {
        //given
        LocalDate date = LocalDate.of(2015, 5, 5);
        Boolean isCompleted = true;
        Long userId = 1L;
        Long raceId = 7L;
        String comment = "take that loser / comment";

        Posts savedPosts = postsRepository.save(Posts.builder()
                .date(date)
                .isCompleted(isCompleted)
                .userId(userId)
                .raceId(raceId)
                .comment(comment)
                .build());

        Long id = savedPosts.getId();

        String url = "http://localhost:" + port + "/api/v1/posts/" + id;

        //when
        mvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(id))
                .andExpect(MockMvcResultMatchers.jsonPath("$.comment").value(comment))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isCompleted").value(isCompleted.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId").value(userId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.raceId").value(raceId));
//                .andDo(print());
//        ResponseEntity<PostsResponseDto> responseEntity2 = restTemplate.getForEntity(url,PostsResponseDto.class);

        //then
//        assertThat(responseEntity2.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(responseEntity2.getBody().getAuthor()).isEqualTo(author);
//        assertThat(responseEntity2.getBody().getDate()).isEqualTo(date);
//        assertThat(responseEntity2.getBody().getComment()).isEqualTo(comment);
//        assertThat(responseEntity2.getBody().getIsCompleted()).isEqualTo(isCompleted);
    }

    @Test
    @WithMockUser(roles = "USER")
    public void Posts_can_be_updated() throws Exception {
        //given
        String raceName = "The epic battle of two alpha baboons";
        String wager = "7 Tons of bananas and the position of alpha baboon";
        LocalDate start = LocalDate.now();
        LocalDate end = LocalDate.now().plusMonths(1);
        Long userId = 1L;
        String fstHabit = "To workout at least 10 minutes every day";

        Long raceId = raceRepository.save(Race.builder()
                .raceName(raceName)
                .wager(wager)
                .startDate(start)
                .endDate(end)
                .fstUserId(userId)
                .fstUserHabit(fstHabit)
                .build()).getId();

        LocalDate date = LocalDate.now();
        Boolean isCompleted = true;
        String comment = "take that loser / comment";

        Posts savedPosts = postsRepository.save(Posts.builder()
                .date(date)
                .isCompleted(isCompleted)
                .userId(userId)
                .raceId(raceId)
                .comment(comment)
                .build());

        Long id = savedPosts.getId();

        String updatedComment = "I was lying lol";

        PostsUpdateRequestDto requestDto = PostsUpdateRequestDto.builder()
                .date(date)
                .comment(updatedComment)
                .build();

        HttpEntity<PostsUpdateRequestDto> requestEntity = new HttpEntity<>(requestDto);

        String url = "http://localhost:" + port + "/api/v1/posts/" + id;

        //when
        mvc.perform(put(url)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk());
//                .andDo(print());
        //ResponseEntity<Long> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Long.class);

        //then
        //assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        //assertThat(responseEntity.getBody()).isEqualTo(id);

        List<Posts> all = postsRepository.findAll();
        assertThat(all.get(0).getComment()).isEqualTo(updatedComment);
    }

    //misc
    @Test
    @WithMockUser(roles = "USER")
    public void Posts_can_all_be_getted() throws Exception {
        //given
        LocalDate date = LocalDate.of(2020,2,2);
        Boolean isCompleted = true;
        Long userId = 5L;
        Long raceId = 90L;
        String comment = "take that loser / comment";

        postsRepository.save(Posts.builder()
                .date(date)
                .isCompleted(isCompleted)
                .userId(userId)
                .raceId(raceId)
                .comment(comment)
                .build());

        postsRepository.save(Posts.builder()
                .date(date)
                .isCompleted(!isCompleted)
                .userId(userId)
                .raceId(raceId)
                .comment(comment+" - part 2")
                .build());

        String url = "http://localhost:" + port + "/api/v1/posts/all";

        //when
        MvcResult res = mvc.perform(get(url))
                .andExpect(status().isOk())
                .andReturn();

        System.out.println(">>>>"+res.getResponse().getContentAsString());

        //ResponseEntity<PostsResponseDto[]> responseEntity2 = restTemplate.getForEntity(url,PostsResponseDto[].class);

        //then
        //assertThat(responseEntity2.getStatusCode()).isEqualTo(HttpStatus.OK);
        //PostsResponseDto firstDto = responseEntity2.getBody()[0];
        //PostsResponseDto secondDto = responseEntity2.getBody()[1];

        //assertThat(firstDto.getAuthor()).isEqualTo(author);
        //assertThat(firstDto.getDate()).isEqualTo(date);
        //assertThat(firstDto.getComment()).isEqualTo(comment);
        //assertThat(firstDto.getIsCompleted()).isEqualTo(isCompleted);

        //assertThat(secondDto.getAuthor()).isEqualTo(author);
        //assertThat(secondDto.getDate()).isEqualTo(date);
        //assertThat(secondDto.getComment()).isEqualTo(comment+" - part 2");
        //assertThat(secondDto.getIsCompleted()).isEqualTo(!isCompleted);
    }
}