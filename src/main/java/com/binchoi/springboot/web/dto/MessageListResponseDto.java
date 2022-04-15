package com.binchoi.springboot.web.dto;

import com.binchoi.springboot.domain.posts.Posts;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class MessageListResponseDto {
    private String userName;
    private String comment;
    private LocalDate createdDate;

    public MessageListResponseDto(Posts entity, String userName) {
        this.userName = userName;
        this.comment = entity.getComment();
        this.createdDate = LocalDate.from(entity.getCreatedDate());
    }
}

