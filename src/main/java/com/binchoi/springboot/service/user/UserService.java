package com.binchoi.springboot.service.user;

import com.binchoi.springboot.domain.race.Race;
import com.binchoi.springboot.domain.user.User;
import com.binchoi.springboot.domain.user.UserRepository;
import com.binchoi.springboot.web.dto.RaceResponseDto;
import com.binchoi.springboot.web.dto.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public UserResponseDto findById(Long id) {
        User entity = userRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("The user does not exist. id="+ id));
        return new UserResponseDto(entity);
    }
}
