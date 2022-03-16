package com.binchoi.springboot.web.dto;

import com.binchoi.springboot.domain.posts.Posts;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class PostsResponseDto {

    private Long id;
    private LocalDate date;
    private Boolean isCompleted;
    private String author;
    private String comment;

    @JsonCreator
    public PostsResponseDto(Posts entity) {
        this.id = entity.getId();
        this.date = entity.getDate();
        this.isCompleted = entity.getIsCompleted();
        this.author = entity.getAuthor();
        this.comment = entity.getComment();
    }
}
