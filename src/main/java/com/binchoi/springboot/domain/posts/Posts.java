package com.binchoi.springboot.domain.posts;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

// check-in's for habits
@Getter
@NoArgsConstructor
@Entity
public class Posts {

    // primary key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private boolean completed;

    private String author;

    // optional - nullable=true
    @Column(columnDefinition = "TEXT")
    private String content;

    @Builder // more explicit constructor that can help locate errors before runtime!
    public Posts(boolean completed, String content, String author) {
        this.completed = completed;
        this.content = content; // may be null
        this.author = author;
    }



}
