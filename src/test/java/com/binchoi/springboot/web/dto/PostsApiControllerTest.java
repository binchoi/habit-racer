package com.binchoi.springboot.web.dto;

import com.binchoi.springboot.domain.exception.CustomValidationException;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertTrue;
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

        // A race is required to post/put/delete/get posts
        String raceName = "The epic battle of two alpha baboons";
        String wager = "7 Tons of bananas and the position of alpha baboon";
        LocalDate start = LocalDate.of(2020,2,2);
        LocalDate end = LocalDate.of(2020,3,2);
        Long userId = 1L;
        String fstHabit = "To workout at least 10 minutes every day";

        raceRepository.save(Race.builder()
                .raceName(raceName)
                .wager(wager)
                .startDate(start)
                .endDate(end)
                .fstUserId(userId)
                .fstUserHabit(fstHabit)
                .build());
    }

    @After
    public void tearDown() throws Exception {
        postsRepository.deleteAll();
        raceRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles = "USER")
    public void Posts_can_be_posted() throws Exception {
        //given
        Race entity = raceRepository.findAll().get(0);
        Long raceId = entity.getId();
        LocalDate start = entity.getStartDate();

        Long userId = 7L;
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

        //then

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

        Long id = postsRepository.save(Posts.builder()
                .date(date)
                .isCompleted(isCompleted)
                .userId(userId)
                .raceId(raceId)
                .comment(comment)
                .build()).getId();

        String url = "http://localhost:" + port + "/api/v1/posts/" + id;

        //when
        mvc.perform(get(url))
                .andExpect(status().isOk()) //then
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(id))
                .andExpect(MockMvcResultMatchers.jsonPath("$.comment").value(comment))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isCompleted").value(isCompleted.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId").value(userId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.raceId").value(raceId));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void Posts_can_be_updated() throws Exception {
        //given
        Race entity = raceRepository.findAll().get(0);
        Long raceId = entity.getId();
        LocalDate start = entity.getStartDate();

        Long userId = 7L;
        LocalDate date = start.plusDays(1);
        Boolean isCompleted = true;
        String comment = "take that loser / comment";

        Long id = postsRepository.save(Posts.builder()
                .date(date)
                .isCompleted(isCompleted)
                .userId(userId)
                .raceId(raceId)
                .comment(comment)
                .build()).getId();

        String updatedComment = "I am sorry for the mean comment";

        PostsUpdateRequestDto requestDto = PostsUpdateRequestDto.builder()
                .date(date)
                .comment(updatedComment)
                .build();

        String url = "http://localhost:" + port + "/api/v1/posts/" + id;

        //when
        mvc.perform(put(url)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk());

        //then
        List<Posts> all = postsRepository.findAll();
        assertThat(all.get(0).getComment()).isEqualTo(updatedComment);
    }

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
                .date(date.plusDays(1))
                .isCompleted(!isCompleted)
                .userId(userId)
                .raceId(raceId)
                .comment(comment+" - part 2")
                .build());

        String url = "http://localhost:" + port + "/api/v1/posts";

        //when
        mvc.perform(get(url))
                .andExpect(status().isOk()) //then
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].userId").value(userId))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].comment").value(comment))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].date").value(date.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].userId").value(userId))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].comment").value(comment+" - part 2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].date").value(date.plusDays(1).toString()));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void Posts_cannot_be_posted_without_comment() throws Exception {
        //given
        Race entity = raceRepository.findAll().get(0);
        Long raceId = entity.getId();
        LocalDate start = entity.getStartDate();

        Long userId = 7L;
        Boolean isCompleted = true;
        PostsSaveRequestDto requestDto = PostsSaveRequestDto.builder()
                .date(start)
                .userId(userId)
                .raceId(raceId)
                .isCompleted(isCompleted)
                .build();

        String url = "http://localhost:" + port + "/api/v1/posts";

        //when
        mvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(requestDto))) //then
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertThat(result.getResolvedException().getMessage())
                        .contains("Please write a message to motivate your competitor."));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void Posts_cannot_be_updated_without_comments() throws Exception {
        //given
        Race entity = raceRepository.findAll().get(0);
        Long raceId = entity.getId();
        LocalDate start = entity.getStartDate();

        Long userId = 7L;
        LocalDate date = start.plusDays(1);
        Boolean isCompleted = true;
        String comment = "take that loser / comment";

        Long id = postsRepository.save(Posts.builder()
                .date(date)
                .isCompleted(isCompleted)
                .userId(userId)
                .raceId(raceId)
                .comment(comment)
                .build()).getId();

        PostsUpdateRequestDto requestDto = PostsUpdateRequestDto.builder()
                .date(date)
                .build();

        String url = "http://localhost:" + port + "/api/v1/posts/" + id;

        //when
        mvc.perform(put(url)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(requestDto))) //then
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertThat(result.getResolvedException().getMessage())
                        .contains("Please write a message to motivate your competitor."));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void Posts_dating_before_race_StartDate_cannot_be_posted() throws Exception {
        //given
        Race entity = raceRepository.findAll().get(0);
        Long raceId = entity.getId();
        LocalDate start = entity.getStartDate();

        Long userId = 7L;
        Boolean isCompleted = true;
        String comment = "take that loser / comment";
        PostsSaveRequestDto requestDto = PostsSaveRequestDto.builder()
                .comment(comment)
                .date(start.minusDays(1))
                .userId(userId)
                .raceId(raceId)
                .isCompleted(isCompleted)
                .build();

        String url = "http://localhost:" + port + "/api/v1/posts";

        //when
        mvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(requestDto))) //then
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof CustomValidationException));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void Posts_dating_in_the_future_cannot_be_posted() throws Exception {
        //given
        Race entity = raceRepository.findAll().get(0);
        Long raceId = entity.getId();

        Long userId = 7L;
        Boolean isCompleted = true;
        String comment = "take that loser / comment";
        PostsSaveRequestDto requestDto = PostsSaveRequestDto.builder()
                .comment(comment)
                .date(LocalDate.now().plusDays(1))
                .userId(userId)
                .raceId(raceId)
                .isCompleted(isCompleted)
                .build();

        String url = "http://localhost:" + port + "/api/v1/posts";

        //when
        mvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(requestDto))) //then
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof CustomValidationException));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void Duplicate_date_post_cannot_be_posted() throws Exception {
        //given
        Race entity = raceRepository.findAll().get(0);
        Long raceId = entity.getId();
        LocalDate start = entity.getStartDate();

        Long userId = 7L;
        LocalDate date = start.plusDays(1);
        Boolean isCompleted = true;
        String comment = "take that loser / comment";

        postsRepository.save(Posts.builder()
                .date(date)
                .isCompleted(isCompleted)
                .userId(userId)
                .raceId(raceId)
                .comment(comment)
                .build());

        PostsSaveRequestDto requestDto = PostsSaveRequestDto.builder()
                .date(date)           // two posts dating the same day
                .isCompleted(isCompleted)
                .userId(userId)
                .raceId(raceId)
                .comment(comment)
                .build();

        String url = "http://localhost:" + port + "/api/v1/posts";

        //when
        mvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(requestDto))) //then
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof CustomValidationException));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void Posts_to_nonexistent_race_cannot_be_posted() throws Exception {
        //given
        Long raceId = 18L; // this race does not exist
        Long userId = 7L;
        Boolean isCompleted = true;
        String comment = "take that loser / comment";

        PostsSaveRequestDto requestDto = PostsSaveRequestDto.builder()
                .comment(comment)
                .date(LocalDate.of(2020,2,19))
                .userId(userId)
                .raceId(raceId)
                .isCompleted(isCompleted)
                .build();

        String url = "http://localhost:" + port + "/api/v1/posts";

        //when
        assertThatThrownBy(() ->
                mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(objectMapper.writeValueAsString(requestDto))) //then
        ).hasCause(new IllegalArgumentException("The race does not exist. id="+raceId));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void Nonexistent_post_cannot_be_updated() throws Exception {
        //given
        Long id = 100L;
        String updatedComment = "I am sorry for the mean comment";
        LocalDate date = LocalDate.of(2020,2,2);

        String url = "http://localhost:" + port + "/api/v1/posts/" + id;

        PostsUpdateRequestDto requestDto = PostsUpdateRequestDto.builder()
                .date(date)
                .comment(updatedComment)
                .build();

        //when
        assertThatThrownBy(() ->
                mvc.perform(put(url)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(objectMapper.writeValueAsString(requestDto))) //then
        ).hasCause(new IllegalArgumentException("The post does not exist. id=" + id));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void Posts_cannot_be_updated_to_date_before_race_StartDate() throws Exception {
        //given
        Race entity = raceRepository.findAll().get(0);
        Long raceId = entity.getId();
        LocalDate start = entity.getStartDate();

        Long userId = 7L;
        LocalDate date = start.plusDays(1);
        Boolean isCompleted = true;
        String comment = "take that loser / comment";

        Long id = postsRepository.save(Posts.builder()
                .date(date)
                .isCompleted(isCompleted)
                .userId(userId)
                .raceId(raceId)
                .comment(comment)
                .build()).getId();

        String url = "http://localhost:" + port + "/api/v1/posts/" + id;

        PostsUpdateRequestDto requestDto = PostsUpdateRequestDto.builder()
                .date(start.minusDays(1)) // update date to before race start date
                .comment(comment)
                .build();

        //when
        mvc.perform(put(url)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(requestDto))) //then
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof CustomValidationException));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void Posts_cannot_be_updated_to_date_in_future() throws Exception {
        //given
        Race entity = raceRepository.findAll().get(0);
        Long raceId = entity.getId();
        LocalDate start = entity.getStartDate();

        Long userId = 7L;
        LocalDate date = start.plusDays(1);
        Boolean isCompleted = true;
        String comment = "take that buddy!";

        Long id = postsRepository.save(Posts.builder()
                .date(date)
                .isCompleted(isCompleted)
                .userId(userId)
                .raceId(raceId)
                .comment(comment)
                .build()).getId();

        String url = "http://localhost:" + port + "/api/v1/posts/" + id;

        PostsUpdateRequestDto requestDto = PostsUpdateRequestDto.builder()
                .date(LocalDate.now().plusDays(1)) // update date to future
                .comment(comment)
                .build();

        //when
        mvc.perform(put(url)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(requestDto))) //then
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof CustomValidationException));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void Posts_cannot_be_updated_to_duplicate_date_entry() throws Exception {
        //given
        Race entity = raceRepository.findAll().get(0);
        Long raceId = entity.getId();
        LocalDate start = entity.getStartDate();

        Long userId = 7L;
        LocalDate date = start.plusDays(1);
        Boolean isCompleted = true;
        String comment = "Haha I am ahead";

        postsRepository.save(Posts.builder()
                .date(date)
                .isCompleted(isCompleted)
                .userId(userId)
                .raceId(raceId)
                .comment(comment)
                .build());

        Long id = postsRepository.save(Posts.builder()
                .date(date.plusDays(1))
                .isCompleted(isCompleted)
                .userId(userId)
                .raceId(raceId)
                .comment(comment)
                .build()).getId();

        String url = "http://localhost:" + port + "/api/v1/posts/" + id;

        PostsUpdateRequestDto requestDto = PostsUpdateRequestDto.builder()
                .date(date) // update date to result in duplicate entry
                .comment(comment)
                .build();

        //when
        mvc.perform(put(url)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(requestDto))) //then
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof CustomValidationException));
    }
}