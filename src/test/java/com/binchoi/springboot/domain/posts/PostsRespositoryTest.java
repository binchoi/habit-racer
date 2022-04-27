package com.binchoi.springboot.domain.posts;

import com.binchoi.springboot.domain.posts.Posts;
import com.binchoi.springboot.domain.posts.PostsRepository;
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
public class PostsRespositoryTest {

    @Autowired
    PostsRepository postsRepository;

    @After
    public void cleanup() {
        postsRepository.deleteAll();
    }

    @Test
    public void get_Posts() {
        //given
        LocalDate date = LocalDate.of(2019,8,8);
        Boolean isCompleted = true;
        String comment = "Haha take that. I am ahead";

        postsRepository.save(Posts.builder()
                .date(date)
                .isCompleted(isCompleted)
                .comment(comment)
                .userId(33L)
                .raceId(1L)
                .build());

        //when
        List<Posts> postsList = postsRepository.findAll();

        //then
        Posts posts = postsList.get(0);
        assertThat(posts.getDate()).isEqualTo(date);
        assertThat(posts.getIsCompleted()).isEqualTo(isCompleted);
        assertThat(posts.getComment()).isEqualTo(comment);
        assertThat(posts.getRaceId()).isEqualTo(1L);
    }

    @Test
    public void BaseTimeEntity_Test() {
        //given
        LocalDateTime now = LocalDateTime.of(2019,6,4,0,0,0);
        postsRepository.save(Posts.builder()
                .userId(3L)
                .raceId(33L)
                .isCompleted(true)
                .date(LocalDate.of(2019,6,5))
                .comment("YOLO")
                .build());

        //when
        List<Posts> postsList = postsRepository.findAll();

        //then
        Posts posts = postsList.get(0);

//        System.out.println(">>>>>>>>> createDate="+posts.getCreatedDate()+", modifiedDate="+posts.getModifiedDate());

        assertThat(posts.getCreatedDate()).isAfter(now);
        assertThat(posts.getModifiedDate()).isAfter(now);
        assertThat(posts.getRaceId()).isEqualTo(33L);
        //misc
//        assertThat(posts.getModifiedDate().getYear()).isEqualTo(LocalDateTime.now().getYear());
//        assertThat(posts.getModifiedDate().getMonth()).isEqualTo(LocalDateTime.now().getMonth());
//        assertThat(posts.getModifiedDate().getDayOfMonth()).isEqualTo(LocalDateTime.now().getDayOfMonth());
    }

}
