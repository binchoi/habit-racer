package com.binchoi.springboot.service.posts;

import com.binchoi.springboot.domain.posts.Posts;
import com.binchoi.springboot.domain.posts.PostsRepository;
import com.binchoi.springboot.web.dto.PostsListResponseDto;
import com.binchoi.springboot.web.dto.PostsResponseDto;
import com.binchoi.springboot.web.dto.PostsSaveRequestDto;
import com.binchoi.springboot.web.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostsService {
    private final PostsRepository postsRepository;

    @Transactional
    public Long save(PostsSaveRequestDto requestDto) {
        return postsRepository.save(requestDto.toEntity()).getId();
    }

    @Transactional
    public PostsResponseDto findById(Long id) {
        Posts entity = postsRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("The post does not exist. id=" + id));
        return new PostsResponseDto(entity);
    }

    @Transactional
    public Long update(Long id, PostsUpdateRequestDto requestDto) {
        Posts posts = postsRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("The post does not exist. id=" + id));
        posts.update(requestDto.getDate(), requestDto.getComment());
        return id;
    }

    @Transactional
    public void delete(Long id) {
        Posts posts = postsRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("The post does not exist. id=" + id));

        postsRepository.delete(posts);
    }

    //misc
    @Transactional
    public PostsResponseDto[] findAll() {
        List<Posts> entityList = postsRepository.findAll();
        return entityList.stream()
                .map(PostsResponseDto::new)
                .toArray(PostsResponseDto[]::new);
    }

    @Transactional(readOnly = true)
    public List<PostsListResponseDto> findByUserId(Long userId) {
        return postsRepository.findByUserId(userId).stream()
                .map(PostsListResponseDto::new)
                .collect(Collectors.toList());
    }

}
