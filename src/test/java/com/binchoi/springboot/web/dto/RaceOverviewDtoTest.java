package com.binchoi.springboot.web.dto;

import com.binchoi.springboot.domain.posts.Posts;
import com.binchoi.springboot.domain.posts.PostsRepository;
import com.binchoi.springboot.domain.race.RaceRepository;
import com.binchoi.springboot.domain.user.Role;
import com.binchoi.springboot.domain.user.User;
import com.binchoi.springboot.domain.user.UserRepository;
import com.binchoi.springboot.service.posts.PostsService;
import com.binchoi.springboot.service.race.RaceService;
import com.binchoi.springboot.service.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpSession;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RaceOverviewDtoTest {

    @LocalServerPort
    private int port;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private RaceRepository raceRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PostsService postsService;

    @Autowired
    private RaceService raceService;

    private MockMvc mvc;

    private static User userEntity;

    @Before
    public void setup() {
        if (userEntity==null) {
            userEntity = User.builder()
                    .email("some-email@gmail.com")
                    .name("tester")
                    .picture("https://get_my_picture.com")
                    .role(Role.USER)
                    .build();

            userRepository.save(userEntity);
        }

        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @After
    public void tearDown() throws Exception {
        raceRepository.deleteAll();
    }

    @Test
    public void Basic_raceOverviewDto_can_be_constructed_and_its_attributes_retrieved() {
        //given
        String raceName = "The epic battle of two alpha baboons";
        String wager = "7 Tons of bananas and the position of alpha baboon";
        LocalDate start = LocalDate.now();
        LocalDate end = LocalDate.now().plusMonths(1);
        Long id = userEntity.getId();
        String fstHabit = "To workout at least 10 minutes every day";

        RaceSaveRequestDto requestDto = RaceSaveRequestDto.builder()
                .raceName(raceName)
                .wager(wager)
                .startDate(start)
                .endDate(end)
                .fstUserId(id)
                .fstUserHabit(fstHabit)
                .build();

        Long raceId = raceRepository.save(requestDto.toEntity()).getId();
        RaceResponseDto race = raceService.findById(raceId);
        RaceTimeInfoDto raceTimeInfo = new RaceTimeInfoDto(race);
        Long sndUserId = race.getSndUserId();

        //when
        RaceOverviewDto raceOverview = new RaceOverviewDto(
                userService.findById(id).getName(),
                sndUserId!=null ? userService.findById(sndUserId).getName() : "TBD",
                postsService.findByUserIdRaceId(id, raceId),
                sndUserId!=null ? postsService.findByUserIdRaceId(sndUserId, id) : new ArrayList<>(),
                race,
                raceTimeInfo.getDaysFromStart().intValue());

        //then
        assertThat(raceOverview.getFstUserId()).isEqualTo(userEntity.getId());
        assertThat(raceOverview.getSndUserId()).isNull();
        assertThat(raceOverview.getFstUserName()).isEqualTo(userEntity.getName());
        assertThat(raceOverview.getSndUserName()).isEqualTo("TBD");
        assertThat(raceOverview.getFstUserPosts()).isEmpty();
        assertThat(raceOverview.getSndUserPosts()).isEmpty();
        assertThat(raceOverview.getId()).isEqualTo(raceId);
        assertThat(raceOverview.getFstUserHabit()).isEqualTo(fstHabit);
        assertThat(raceOverview.getSndUserHabit()).isEqualTo("TBD");
        assertThat(raceOverview.getFstUserSuccessCount()).isEqualTo(0);
        assertThat(raceOverview.getSndUserSuccessCount()).isEqualTo(0);
        assertThat(raceOverview.getFstUserSuccessPercent()).isEqualTo(0);
        assertThat(raceOverview.getSndUserSuccessPercent()).isEqualTo(0);
        assertThat(raceOverview.getFstUserVictory()).isFalse();
        assertThat(raceOverview.getWinnerName()).isEqualTo("TBD");
        assertThat(raceOverview.getLoserName()).isEqualTo(userEntity.getName());
        assertThat(raceOverview.getPostDifference()).isEqualTo(0);
    }

    @Test
    public void raceOverviewDto_of_fstUser_winning_behaves_appropriately() {
        //given
        String raceName = "The epic battle of two alpha baboons";
        String wager = "7 Tons of bananas and the position of alpha baboon";
        LocalDate start = LocalDate.of(2010,1,1);
        LocalDate end = LocalDate.of(2010,2,1);
        Long id = userEntity.getId();
        String fstHabit = "To workout at least 10 minutes every day";

        RaceSaveRequestDto requestDto = RaceSaveRequestDto.builder()
                .raceName(raceName)
                .wager(wager)
                .startDate(start)
                .endDate(end)
                .fstUserId(id)
                .fstUserHabit(fstHabit)
                .build();

        Long raceId = raceRepository.save(requestDto.toEntity()).getId();
        RaceResponseDto race = raceService.findById(raceId);
        RaceTimeInfoDto raceTimeInfo = new RaceTimeInfoDto(race);
        Long sndUserId = race.getSndUserId();

        for (int i=1; i < 11; i++) {
            postsRepository.save(Posts.builder()
                    .date(LocalDate.of(2010,1,i))
                    .isCompleted(true)
                    .userId(id)
                    .raceId(raceId)
                    .comment("comment")
                    .build());
        }

        //when
        RaceOverviewDto raceOverview = new RaceOverviewDto(
                userService.findById(id).getName(),
                sndUserId!=null ? userService.findById(sndUserId).getName() : "TBD",
                postsService.findByUserIdRaceId(id, raceId),
                sndUserId!=null ? postsService.findByUserIdRaceId(sndUserId, id) : new ArrayList<>(),
                race,
                raceTimeInfo.getDaysFromStart().intValue());

        //then
        assertThat(raceOverview.getFstUserId()).isEqualTo(userEntity.getId());
        assertThat(raceOverview.getSndUserId()).isNull();
        assertThat(raceOverview.getFstUserName()).isEqualTo(userEntity.getName());
        assertThat(raceOverview.getSndUserName()).isEqualTo("TBD");
        assertThat(raceOverview.getFstUserPosts()).isNotEmpty();
        assertThat(raceOverview.getSndUserPosts()).isEmpty();
        assertThat(raceOverview.getFstUserPosts().size()).isEqualTo(10);
        assertThat(raceOverview.getId()).isEqualTo(raceId);
        assertThat(raceOverview.getFstUserHabit()).isEqualTo(fstHabit);
        assertThat(raceOverview.getSndUserHabit()).isEqualTo("TBD");
        assertThat(raceOverview.getFstUserSuccessCount()).isEqualTo(10);
        assertThat(raceOverview.getSndUserSuccessCount()).isEqualTo(0);
        assertThat(raceOverview.getFstUserSuccessPercent()).isEqualTo(1000/raceTimeInfo.getDaysFromStart());
        assertThat(raceOverview.getSndUserSuccessPercent()).isEqualTo(0);
        assertThat(raceOverview.getFstUserVictory()).isTrue();
        assertThat(raceOverview.getWinnerName()).isEqualTo(userEntity.getName());
        assertThat(raceOverview.getLoserName()).isEqualTo("TBD");
        assertThat(raceOverview.getPostDifference()).isEqualTo(10);
    }

}
