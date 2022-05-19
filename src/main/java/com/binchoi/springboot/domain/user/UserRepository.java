package com.binchoi.springboot.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    // for tester
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update User u set u.id = ?2  where u.id = ?1")
    void updateTesterId(Long currId, Long testerId);
}
