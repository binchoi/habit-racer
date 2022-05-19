package com.binchoi.springboot.domain.race;

import com.binchoi.springboot.domain.posts.Posts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RaceRepository extends JpaRepository<Race, Long> {

    @Query("SELECT r FROM Race r WHERE r.fstUserId = ?1 OR r.sndUserId = ?1 ORDER BY r.startDate DESC")
    List<Race> findByUserId(Long userId);

}
