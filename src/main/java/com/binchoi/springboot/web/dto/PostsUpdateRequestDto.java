package com.binchoi.springboot.web.dto;

import com.binchoi.springboot.domain.posts.Posts;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class PostsUpdateRequestDto {

    private LocalDate date;
    private String comment;

    @Builder
    public PostsUpdateRequestDto(LocalDate date, String comment) {
        this.date = date;
        this.comment = comment;
    }

}
