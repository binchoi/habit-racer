package com.binchoi.springboot.web.dto;

import com.binchoi.springboot.domain.posts.Posts;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class PostsListResponseDto {
    private Long id;
    private LocalDate date;
    private Boolean isComplicated;
    private Long userId;
    private String comment;
    private LocalDateTime modifiedDate;

    public PostsListResponseDto(Posts entity) {
        this.id = entity.getId();
        this.date = entity.getDate();
        this.isComplicated = entity.getIsCompleted();
        this.userId = entity.getUserId();
        this.comment = entity.getComment();
        this.modifiedDate = entity.getModifiedDate();
    }
}
