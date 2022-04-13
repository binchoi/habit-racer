package com.binchoi.springboot.web.dto;

import com.binchoi.springboot.domain.posts.Posts;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class PostsSaveRequestDto {
    private LocalDate date;
    private Boolean isCompleted;
    private Long userId;
    private Long raceId;

    @NotBlank(message = "Please write a message to motivate your competitor.")
    private String comment;

    @Builder
    public PostsSaveRequestDto(LocalDate date, Boolean isCompleted, Long userId, Long raceId, String comment) {
        this.date = date;
        this.isCompleted = isCompleted;
        this.userId = userId;
        this.raceId = raceId;
        this.comment = comment;
    }

    public Posts toEntity() {
        return Posts.builder()
                .date(date)
                .isCompleted(isCompleted)
                .userId(userId)
                .raceId(raceId)
                .comment(comment)
                .build();
    }
}
