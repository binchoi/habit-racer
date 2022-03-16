package com.binchoi.springboot.domain.posts;

import com.binchoi.springboot.domain.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

// check-in's for habits
@Getter
@NoArgsConstructor
@Entity
public class Posts extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private Boolean isCompleted;

    private String author; // replace with author id later on?

    @Column(columnDefinition = "TEXT")
    private String comment;

    @Builder // more explicit constructor that can help locate errors before runtime!
    public Posts(LocalDate date, Boolean isCompleted, String author, String comment) {
        this.date = date;
        this.isCompleted = isCompleted;
        this.author = author;
        this.comment = comment;
    }

    public void update(Boolean isCompleted, String comment) {
        this.isCompleted = isCompleted;
        this.comment = comment;
    }

}
