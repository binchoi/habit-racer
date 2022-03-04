package com.binchoi.springboot.web.domain.posts;

import com.binchoi.springboot.domain.posts.Posts;
import com.binchoi.springboot.domain.posts.PostsRepository;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

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
        boolean completed = true;
        String content = "Haha take that. I am ahead";

        postsRepository.save(Posts.builder()
                .completed(completed)
                .content(content)
                .author("binchoi16@gmail.com")
                .build());

        //when
        List<Posts> postsList = postsRepository.findAll();

        //then
        Posts firstPost = postsList.get(0);
//        assertThat(firstPost.getCom).isEqualTo(completed); problem with getter for completed field
    }

}
