package com.binchoi.springboot.web.dto;

import com.binchoi.springboot.domain.posts.Posts;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class PostsSaveRequestDto {
    private LocalDate date;
    private Boolean isCompleted;
    private Long userId;
    private String comment;

    @Builder
    public PostsSaveRequestDto(LocalDate date, Boolean isCompleted, Long userId, String comment) {
        this.date = date;
        this.isCompleted = isCompleted;
        this.userId = userId;
        this.comment = comment;
    }

    public Posts toEntity() {
        return Posts.builder()
                .date(date)
                .isCompleted(isCompleted)
                .userId(userId)
                .comment(comment)
                .build();
    }
}
