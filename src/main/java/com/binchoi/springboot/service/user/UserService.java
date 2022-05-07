package com.binchoi.springboot.service.user;

import com.binchoi.springboot.domain.posts.Posts;
import com.binchoi.springboot.domain.posts.PostsRepository;
import com.binchoi.springboot.domain.race.Race;
import com.binchoi.springboot.domain.race.RaceRepository;
import com.binchoi.springboot.domain.user.User;
import com.binchoi.springboot.domain.user.UserRepository;
import com.binchoi.springboot.web.dto.RaceResponseDto;
import com.binchoi.springboot.web.dto.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PostsRepository postsRepository;
    private final RaceRepository raceRepository;

    @Transactional
    public UserResponseDto findById(Long id) {
        User entity = userRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("The user does not exist. id="+ id));
        return new UserResponseDto(entity);
    }

    @Transactional
    public void delete(Long id) {
        // delete user entity
        User entity = userRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("The user does not exist. id="+ id));
        userRepository.delete(entity);
        // delete all posts by user
        postsRepository.findByUserId(id)
                .forEach(postsRepository::delete);
        // delete all races with user
        raceRepository.findByUserId(id)
                .forEach(raceRepository::delete);
    }
}
