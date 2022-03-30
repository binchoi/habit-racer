package com.binchoi.springboot.domain.race;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RaceRepository extends JpaRepository<Race, Long> {
//    Optional<Race> findByFstUserId(Long fstUserId);
}
