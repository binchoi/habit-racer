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
    private Boolean isCompleted;
    private Long userId;
    private Long raceId;
    private String comment;
    private LocalDateTime modifiedDate;

    public PostsListResponseDto(Posts entity) {
        this.id = entity.getId();
        this.date = entity.getDate();
        this.isCompleted = entity.getIsCompleted();
        this.userId = entity.getUserId();
        this.raceId = entity.getRaceId();
        this.comment = entity.getComment();
        this.modifiedDate = entity.getModifiedDate();
    }
}
