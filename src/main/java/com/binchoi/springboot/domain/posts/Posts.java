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

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long raceId;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String comment;


    @Builder // more explicit constructor that can help locate errors before runtime!
    public Posts(LocalDate date, Boolean isCompleted, Long userId,  Long raceId, String comment) {
        this.date = date;
        this.isCompleted = isCompleted;
        this.userId = userId;
        this.raceId = raceId;
        this.comment = comment;
    }

    public void update(LocalDate date, String comment) {
        this.date = date;
        this.comment = comment;
    }

}
