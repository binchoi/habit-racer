package com.binchoi.springboot.service.posts;

import com.binchoi.springboot.domain.exception.CustomValidationException;
import com.binchoi.springboot.domain.posts.Posts;
import com.binchoi.springboot.domain.posts.PostsRepository;
import com.binchoi.springboot.domain.race.RaceRepository;
import com.binchoi.springboot.web.dto.PostsListResponseDto;
import com.binchoi.springboot.web.dto.PostsResponseDto;
import com.binchoi.springboot.web.dto.PostsSaveRequestDto;
import com.binchoi.springboot.web.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostsService {
    private final PostsRepository postsRepository;
    private final RaceRepository raceRepository;

    @Transactional
    public Long save(PostsSaveRequestDto requestDto) {
        verifyDate(requestDto.getDate(), requestDto.getRaceId(), requestDto.getUserId(), null);
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
        verifyDate(requestDto.getDate(), posts.getRaceId(), posts.getUserId(), id);
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

    @Transactional(readOnly = true)
    public List<PostsListResponseDto> findByUserIdRaceId(Long userId, Long raceId) {
        return postsRepository.findByUserIdRaceId(userId, raceId).stream()
                .map(PostsListResponseDto::new)
                .collect(Collectors.toList());
    }

    private void verifyDate(LocalDate dateProvided, Long raceId, Long userId, Long postId) {
        LocalDate raceStartDate = raceRepository.findById(raceId)
                .orElseThrow(() -> new IllegalArgumentException("The race does not exist. id=" + raceId))
                .getStartDate();
        if (raceStartDate.isAfter(dateProvided)) {
            throw new CustomValidationException("Any records before the race start date cannot be added.", "date");
        } else if (LocalDate.now().isBefore(dateProvided)) {
            throw new CustomValidationException("Please provide a valid date.", "date");
        }
        //duplicate entry
        boolean isDuplicate = postsRepository.findByUserIdRaceId(userId, raceId)
                .stream() // second condition below allows for comment updates (where date is unchanged)
                .anyMatch(post -> post.getDate().isEqual(dateProvided) && !(post.getId()).equals(postId));
        if (isDuplicate) {
            throw new CustomValidationException("There is already an existing record for this date.", "date");
        }
    }

}
