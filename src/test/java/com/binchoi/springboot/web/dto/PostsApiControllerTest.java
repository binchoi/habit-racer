package com.binchoi.springboot.web.dto;

import com.binchoi.springboot.domain.posts.Posts;
import com.binchoi.springboot.domain.posts.PostsRepository;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PostsApiControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PostsRepository postsRepository;

    @After
    public void tearDown() throws Exception {
        postsRepository.deleteAll();
    }

    @Test
    public void Posts_can_be_posted() throws Exception {
        //given
        LocalDate date = LocalDate.now();
        Boolean isCompleted = true;
        String author = "author";
        String comment = "take that loser / comment";
        PostsSaveRequestDto requestDto = PostsSaveRequestDto.builder()
                .date(date)
                .isCompleted(isCompleted)
                .author(author)
                .comment(comment)
                .build();

        String url = "http://localhost:" + port + "/api/v1/posts";

        //when
        ResponseEntity<Long> responseEntity = restTemplate.postForEntity(url, requestDto, Long.class);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isGreaterThan(0L);

        List<Posts> all = postsRepository.findAll();
        assertThat(all.get(0).getDate()).isEqualTo(date);
        assertThat(all.get(0).getIsCompleted()).isEqualTo(isCompleted);
        assertThat(all.get(0).getAuthor()).isEqualTo(author);
        assertThat(all.get(0).getComment()).isEqualTo(comment);
    }

    @Test
    public void Posts_can_be_getted() throws Exception {
        //given
        LocalDate date = LocalDate.now();
        Boolean isCompleted = true;
        String author = "author";
        String comment = "take that loser / comment";

        Posts savedPosts = postsRepository.save(Posts.builder()
                .date(date)
                .isCompleted(isCompleted)
                .author(author)
                .comment(comment)
                .build());

        Long id = savedPosts.getId();

        String url = "http://localhost:" + port + "/api/v1/posts/" + id;

        //when
        ResponseEntity<PostsResponseDto> responseEntity2 = restTemplate.getForEntity(url,PostsResponseDto.class);

        //then
        assertThat(responseEntity2.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity2.getBody().getAuthor()).isEqualTo(author);
        assertThat(responseEntity2.getBody().getDate()).isEqualTo(date);
        assertThat(responseEntity2.getBody().getComment()).isEqualTo(comment);
        assertThat(responseEntity2.getBody().getIsCompleted()).isEqualTo(isCompleted);
    }

    @Test
    public void Posts_can_be_updated() throws Exception {
        //given
        LocalDate date = LocalDate.now();
        Boolean isCompleted = true;
        String author = "author";
        String comment = "take that loser / comment";

        Posts savedPosts = postsRepository.save(Posts.builder()
                .date(date)
                .isCompleted(isCompleted)
                .author(author)
                .comment(comment)
                .build());

        Long id = savedPosts.getId();

        LocalDate updatedDate = LocalDate.now().minusDays(1);
        String updatedComment = "I was lying lol";

        PostsUpdateRequestDto requestDto = PostsUpdateRequestDto.builder()
                .date(updatedDate)
                .comment(updatedComment)
                .build();
        HttpEntity<PostsUpdateRequestDto> requestEntity = new HttpEntity<>(requestDto);

        String url = "http://localhost:" + port + "/api/v1/posts/" + id;

        //when
        ResponseEntity<Long> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Long.class);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo(id);

        List<Posts> all = postsRepository.findAll();
        assertThat(all.get(0).getComment()).isEqualTo(updatedComment);
        assertThat(all.get(0).getDate()).isEqualTo(updatedDate);
    }

    //misc
    @Test
    public void Posts_can_all_be_getted() throws Exception {
        //given
        LocalDate date = LocalDate.now();
        Boolean isCompleted = true;
        String author = "author";
        String comment = "take that loser / comment";

        postsRepository.save(Posts.builder()
                .date(date)
                .isCompleted(isCompleted)
                .author(author)
                .comment(comment)
                .build());

        postsRepository.save(Posts.builder()
                .date(date)
                .isCompleted(!isCompleted)
                .author(author)
                .comment(comment+" - part 2")
                .build());

        String url = "http://localhost:" + port + "/api/v1/posts/all";

        //when
        ResponseEntity<PostsResponseDto[]> responseEntity2 = restTemplate.getForEntity(url,PostsResponseDto[].class);

        //then
        assertThat(responseEntity2.getStatusCode()).isEqualTo(HttpStatus.OK);
        PostsResponseDto firstDto = responseEntity2.getBody()[0];
        PostsResponseDto secondDto = responseEntity2.getBody()[1];

        assertThat(firstDto.getAuthor()).isEqualTo(author);
        assertThat(firstDto.getDate()).isEqualTo(date);
        assertThat(firstDto.getComment()).isEqualTo(comment);
        assertThat(firstDto.getIsCompleted()).isEqualTo(isCompleted);

        assertThat(secondDto.getAuthor()).isEqualTo(author);
        assertThat(secondDto.getDate()).isEqualTo(date);
        assertThat(secondDto.getComment()).isEqualTo(comment+" - part 2");
        assertThat(secondDto.getIsCompleted()).isEqualTo(!isCompleted);
    }
}
