package com.binchoi.springboot.web;

import com.binchoi.springboot.config.auth.LoginUser;
import com.binchoi.springboot.config.auth.dto.SessionUser;
import com.binchoi.springboot.service.posts.PostsService;
import com.binchoi.springboot.web.dto.PostsResponseDto;
import com.binchoi.springboot.web.dto.PostsSaveRequestDto;
import com.binchoi.springboot.web.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class PostsApiController {

    private final PostsService postsService;

    // consider using postAuthorize (with authentication.principal.name == returnObject)
    // one way
    @PostMapping("/v1/posts")
    public Long save(@Valid @RequestBody PostsSaveRequestDto requestDto) {
        return postsService.save(requestDto);
    }

    @GetMapping("/v1/posts/{id}")
    public PostsResponseDto findById (@PathVariable Long id) {
        return postsService.findById(id);
    }

//    @PreAuthorize("hasPermission(#id, 'posts', 'write')")
    @PutMapping("/v1/posts/{id}")
    public Long update(@PathVariable Long id, @Valid @RequestBody PostsUpdateRequestDto requestDto) {
        return postsService.update(id, requestDto);
    }

//    @PreAuthorize("hasPermission(#id, 'posts', 'write'")
    @DeleteMapping("/v1/posts/{id}")
    public Long delete(@PathVariable Long id) {
        postsService.delete(id);
        return id;
    }

    // Misc
    @GetMapping("/v1/posts/all")
    public PostsResponseDto[] findAll() {
        return postsService.findAll();
    }
}
