package com.binchoi.springboot.web.dto;

import com.binchoi.springboot.domain.posts.Posts;
import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class PostsSaveRequestDto {
    private LocalDate date;
    private Boolean isCompleted;
    private String author;
    private String comment;

    @Builder
    public PostsSaveRequestDto(LocalDate date, Boolean isCompleted, String author, String comment) {
        this.date = date;
        this.isCompleted = isCompleted;
        this.author = author;
        this.comment = comment;
    }

    public Posts toEntity() {
        return Posts.builder()
                .date(date)
                .isCompleted(isCompleted)
                .author(author)
                .comment(comment)
                .build();
    }
}
