package com.binchoi.springboot.web;

import com.binchoi.springboot.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class UserApiController {

    private final UserService userService;

    @DeleteMapping("/v1/user/{id}")
    public Long delete(@PathVariable Long id) {
        userService.delete(id);
        return id;
    }

    // save & update logic and operations are in CustomOAuth2UserService
}
