package com.binchoi.springboot.domain.posts;


import org.springframework.data.jpa.repository.JpaRepository;

// CRUD methods are automatically supported for Posts class -- check out by typing "PostsRepository."
public interface PostsRepository extends JpaRepository<Posts, Long> {
}
