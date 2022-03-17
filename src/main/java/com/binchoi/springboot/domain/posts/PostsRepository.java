package com.binchoi.springboot.domain.posts;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

// CRUD methods are automatically supported for Posts class -- check out by typing "PostsRepository."
public interface PostsRepository extends JpaRepository<Posts, Long> {

    @Query("SELECT p FROM Posts p ORDER BY p.id DESC")
    List<Posts> findAllDesc();

    @Query("SELECT p FROM Posts p WHERE p.author = ?1 ORDER BY p.date DESC")
    List<Posts> findByAuthor(String author); // author for now

}
