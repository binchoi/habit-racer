package com.binchoi.springboot.web.dto;

import com.binchoi.springboot.domain.posts.Posts;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class PostsUpdateRequestDto {

    private Boolean isCompleted;
    private String comment;

    @Builder
    public PostsUpdateRequestDto(Boolean isCompleted, String comment) {
        this.isCompleted = isCompleted;
        this.comment = comment;
    }

}
