package com.binchoi.springboot.web;

import com.binchoi.springboot.config.auth.dto.SessionUser;
import com.binchoi.springboot.domain.posts.Posts;
import com.binchoi.springboot.domain.posts.PostsRepository;
import com.binchoi.springboot.domain.race.Race;
import com.binchoi.springboot.domain.race.RaceRepository;
import com.binchoi.springboot.domain.user.Role;
import com.binchoi.springboot.domain.user.User;
import com.binchoi.springboot.domain.user.UserRepository;
import com.binchoi.springboot.web.auth.WithMockCustomOAuth2User;
import com.binchoi.springboot.web.dto.RaceSaveRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IndexControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RaceRepository raceRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private HttpSession httpSession;

    private MockMvc mvc;

    private static User userEntity;

    private static final String TESTER_ID = "123";

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
            userRepository.updateTesterId(userEntity.getId(), Long.valueOf(TESTER_ID));
            userEntity = userRepository.findById(Long.valueOf(TESTER_ID)).get();
        }

//        assertThat(userEntity.getName()).isEqualTo("tester");
//        assertThat(userEntity.getId()).isEqualTo(Long.valueOf(TESTER_ID));

        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @After
    public void tearDown() throws Exception {
        raceRepository.deleteAll();
//        userRepository.deleteAll();
    }

    @Test
    public void main_page_for_non_authenticated_loading() {
        //given
        List<String> onlyNonSignedInCanSee = Arrays.asList(
                "<a class=\"nav-link\" href=\"#about\">About HabitRacer</a>",
                "<h2>Sample Races</h2>",
                "<p>Please sign-in before joining a race.</p>"
        );

        List<String> onlyUsersCanSee = Arrays.asList(
                "Choose a race and claim the pole.",
                "<h2>Your Races</h2>",
                "<button class=\"btn btn-primary\" id=\"btn-redirect-to-join\">Join</button>"
        );

        //when
        String body = this.restTemplate.getForObject("/", String.class);

        //then
        assertThat(body).contains("HabitRacer");
        assertThat(onlyNonSignedInCanSee.stream().allMatch(body::contains)).isTrue();
        assertThat(onlyUsersCanSee.stream().noneMatch(body::contains)).isTrue();
    }

    @Test
    @WithMockCustomOAuth2User
    public void main_page_for_users_loading() throws Exception {
        //given
        List<String> onlyNonSignedInCanSee = Arrays.asList(
                "<a class=\"nav-link\" href=\"#about\">About HabitRacer</a>",
                "<h2>Sample Races</h2>",
                "<p>Please sign-in before joining a race.</p>"
        );

        List<String> onlyUsersCanSee = Arrays.asList(
                "Choose a race and claim the pole.",
                "<h2>Your Races</h2>",
                "<button class=\"btn btn-primary\" id=\"btn-redirect-to-join\">Join</button>"
        );

        //when
        String pageContent = mvc.perform(get("/")
                .sessionAttr("user", new SessionUser(userEntity)))
        //then
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(onlyNonSignedInCanSee.stream().noneMatch(pageContent::contains)).isTrue();
        assertThat(onlyUsersCanSee.stream().allMatch(pageContent::contains)).isTrue();
    }

    @Test
    @WithMockCustomOAuth2User(userId = TESTER_ID)
    public void main_page_displays_users_races() throws Exception {
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

        raceRepository.save(requestDto.toEntity());

        List<String> pageElements = Arrays.asList(raceName, wager, "Competitor:", "T-", "Enter Race");

        //when
        String pageContent = mvc.perform(get("/")
                        .sessionAttr("user", new SessionUser(userEntity)))
        //then
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(pageElements.stream().allMatch(pageContent::contains)).isTrue();
        assertThat(pageContent.contains("Previous Races")).isFalse(); // no completed race -> no previous race section
    }

    @Test
    @WithMockCustomOAuth2User(userId = TESTER_ID)
    public void main_page_displays_users_completed_races() throws Exception {
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

        raceRepository.save(requestDto.toEntity());

        List<String> pageElements = Arrays.asList(raceName, "Start Date", "End Date", "Previous Races");

        //when
        String pageContent = mvc.perform(get("/")
                        .sessionAttr("user", new SessionUser(userEntity)))
        //then
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(pageElements.stream().allMatch(pageContent::contains)).isTrue();
    }

    @Test
    @WithMockCustomOAuth2User
    public void create_race_page_loading() throws Exception {
        //given
        List<String> pageElements = Arrays.asList(
                "Begin your self-development rally by filling in the following fields.",
                "What is the habit you will build?",
                "Describe the habit and what you hope to accomplish each day in detail"
        );

        //when
        String pageContent = mvc.perform(get("/race/save")
                        .sessionAttr("user", new SessionUser(userEntity)))
        //then
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(pageElements.stream().allMatch(pageContent::contains)).isTrue();
    }

    @Test
    @WithMockCustomOAuth2User(userId = TESTER_ID)
    public void update_race_page_loading() throws Exception {
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

        List<String> pageElements = Arrays.asList(
                "You may update, delete, or extend the race.",
                "You may modify the race name",
                "You may not modify the start date"
        );

        //when
        String pageContent = mvc.perform(get("/race/update/"+raceId)
                        .sessionAttr("user", new SessionUser(userEntity)))
        //then
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(pageElements.stream().allMatch(pageContent::contains)).isTrue();
    }

    @Test
    @WithMockCustomOAuth2User(userId = TESTER_ID)
    public void forbidden_to_update_others_race() throws Exception {
        //given
        String raceName = "The epic battle of two alpha baboons";
        String wager = "7 Tons of bananas and the position of alpha baboon";
        LocalDate start = LocalDate.now();
        LocalDate end = LocalDate.now().plusMonths(1);
        Long id = userEntity.getId()+1;
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

        //when
        mvc.perform(get("/race/update/"+raceId)
                        .sessionAttr("user", new SessionUser(userEntity)))
        //then
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockCustomOAuth2User(userId = TESTER_ID)
    public void update_post_page_loading() throws Exception {
        //given
        Boolean isCompleted = true;
        String comment = "take that loser / comment";

        Long postId = postsRepository.save(Posts.builder()
                .date(LocalDate.of(2020,1,1))
                .isCompleted(isCompleted)
                .userId(userEntity.getId())
                .raceId(12345L)
                .comment(comment)
                .build()).getId();

        List<String> pageElements = Arrays.asList(
                "Make modifications to your previous progress record",
                "Comment/Message for your competitor",
                "The message will be displayed in the race overview page"
        );

        //when
        String pageContent = mvc.perform(get("/posts/update/"+postId)
                        .sessionAttr("user", new SessionUser(userEntity)))
        //then
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(pageElements.stream().allMatch(pageContent::contains)).isTrue();
    }

    @Test
    @WithMockCustomOAuth2User(userId = TESTER_ID)
    public void forbidden_to_view_others_update_post_page() throws Exception {
        //given
        Boolean isCompleted = true;
        String comment = "take that loser / comment";

        Long postId = postsRepository.save(Posts.builder()
                .date(LocalDate.of(2020,1,1))
                .isCompleted(isCompleted)
                .userId(userEntity.getId()+1)
                .raceId(12345L)
                .comment(comment)
                .build()).getId();

        //when
        mvc.perform(get("/posts/update/"+postId)
                        .sessionAttr("user", new SessionUser(userEntity)))
        //then
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockCustomOAuth2User(userId = TESTER_ID)
    public void Save_post_page_loading() throws Exception {
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

        String racePostUrl = "http://localhost:"+port+"/race/"+raceId+"/posts/save";

        List<String> pageElements = Arrays.asList(
                "Document your progress and send a motivational message to your fellow racer",
                "Record your Daily Success",
                "You may record your success of previous days. We trust your honesty"
        );

        //when
        String pageContent = mvc.perform(get(racePostUrl)
                        .sessionAttr("user", new SessionUser(userEntity)))
        //then
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(pageElements.stream().allMatch(pageContent::contains)).isTrue();
    }

    @Test
    @WithMockCustomOAuth2User(userId = TESTER_ID)
    public void Forbidden_to_view_other_race_post_page() throws Exception {
        //given
        String raceName = "The epic battle of two alpha baboons";
        String wager = "7 Tons of bananas and the position of alpha baboon";
        LocalDate start = LocalDate.now();
        LocalDate end = LocalDate.now().plusMonths(1);
        Long id = userEntity.getId()+1;
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
        String racePostUrl = "http://localhost:"+port+"/race/"+raceId+"/posts/save";

        //when
        mvc.perform(get(racePostUrl)
                        .sessionAttr("user", new SessionUser(userEntity)))
        //then
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockCustomOAuth2User(userId = TESTER_ID)
    public void user_profile_page_loading() throws Exception {
        //given
        List<String> userPageElements = Arrays.asList(
                "<div class=\"display-5\">"+userEntity.getName()+"</div>",
                "DRIVER STATS",
                "<th scope=\"row\">Total Race Count</th>",
                "Delete Account"
        );

        System.out.println(userEntity.getId() + "vs" + TESTER_ID); /// non-deterministic

        //when
        String pageContent = mvc.perform(get("/user/"+userEntity.getId())
                .sessionAttr("user", new SessionUser(userEntity)))
        //then
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(userPageElements.stream().allMatch(pageContent::contains)).isTrue();
    }

    @Test
    @WithMockCustomOAuth2User(userId = TESTER_ID)
    public void Race_overview_page_loading() throws Exception {
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
        String urlRaceOverview = "http://localhost:"+port+"/race/"+raceId;

        //when
        String pageContent = mvc.perform(get(urlRaceOverview)
                .sessionAttr("user", new SessionUser(userEntity)))
        //then
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<String> raceOverviewPageElements = Arrays.asList(raceName, wager, fstHabit, userEntity.getName(),
                "You can view all messages sent throughout the competition");

        assertThat(raceOverviewPageElements.stream().allMatch(pageContent::contains)).isTrue();
    }

    @Test
    @WithMockCustomOAuth2User(userId = TESTER_ID)
    public void Completed_race_overview_page_loading() throws Exception {
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

        String urlRaceOverview = "http://localhost:"+port+"/race/"+raceId;

        //when
        String pageContent = mvc.perform(get(urlRaceOverview)
                        .sessionAttr("user", new SessionUser(userEntity)))
        //then
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<String> raceOverviewPageElements = Arrays.asList(raceName, wager,
                fstHabit, userEntity.getName(),
                "Congratulations on finishing the race!",
                "The curtain has come down on the epic battle '"+raceName+"'"
        );

        List<String> ongoingRaceOverviewPageElements = Arrays.asList(
                "Have you completed your habit today?",
                "✨"
        );

        assertThat(raceOverviewPageElements.stream().allMatch(pageContent::contains)).isTrue();
        assertThat(ongoingRaceOverviewPageElements.stream().noneMatch(pageContent::contains)).isTrue();
    }

    @Test
    @WithMockCustomOAuth2User(userId = TESTER_ID)
    public void Forbidden_to_view_others_race_overview_page() throws Exception {
        //given
        String raceName = "The epic battle of two alpha baboons";
        String wager = "7 Tons of bananas and the position of alpha baboon";
        LocalDate start = LocalDate.of(2010,1,1);
        LocalDate end = LocalDate.of(2010,2,1);
        Long id = userEntity.getId()+1;
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

        String urlRaceOverview = "http://localhost:"+port+"/race/"+raceId;

        //when
        mvc.perform(get(urlRaceOverview)
                .sessionAttr("user", new SessionUser(userEntity)))
        //then
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockCustomOAuth2User(userId = TESTER_ID)
    public void forbidden_to_view_others_user_profile_page() throws Exception {
        //when
        mvc.perform(get("/user/"+(userEntity.getId()+1))
                .sessionAttr("user", new SessionUser(userEntity)))
        //then
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockCustomOAuth2User(userId = TESTER_ID)
    public void Join_race_page_loading() throws Exception {
        //given
        User anotherUser = User.builder()
                .email("some-email@gmail.com")
                .name("test-user")
                .picture("https://get_my_picture.com")
                .role(Role.USER)
                .build();
        userRepository.save(anotherUser);

        String raceName = "The epic battle of two alpha baboons";
        String wager = "7 Tons of bananas and the position of alpha baboon";
        LocalDate start = LocalDate.now();
        LocalDate end = LocalDate.now().plusMonths(1);
        Long id = anotherUser.getId();
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

        String joinRaceUrl = "http://localhost:"+port+"/race/join/"+raceId;

        List<String> pageElements = Arrays.asList(
                "You have been challenged by "+anotherUser.getName(),
                raceName,
                wager,
                "You have the final say of when the race ends."
        );

        //when
        String pageContent = mvc.perform(get(joinRaceUrl)
                        .sessionAttr("user", new SessionUser(userEntity)))
        //then
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(pageElements.stream().allMatch(pageContent::contains)).isTrue();
    }

    @Test
    @WithMockCustomOAuth2User(userId = TESTER_ID)
    public void Forbidden_to_view_join_race_page_for_own_race() throws Exception {
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

        String joinRaceUrl = "http://localhost:"+port+"/race/join/"+raceId;

        //when
        mvc.perform(get(joinRaceUrl)
                        .sessionAttr("user", new SessionUser(userEntity)))
        //then
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockCustomOAuth2User(userId = TESTER_ID)
    public void Forbidden_to_view_join_race_page_for_full_race() throws Exception {
        //given
        User anotherUser = User.builder()
                .email("some-email@gmail.com")
                .name("test-user")
                .picture("https://get_my_picture.com")
                .role(Role.USER)
                .build();

        userRepository.save(anotherUser);

        String raceName = "The epic battle of two alpha baboons";
        String wager = "7 Tons of bananas and the position of alpha baboon";
        LocalDate start = LocalDate.now();
        LocalDate end = LocalDate.now().plusMonths(1);
        Long id = anotherUser.getId();
        String fstHabit = "To workout at least 10 minutes every day";

        RaceSaveRequestDto requestDto = RaceSaveRequestDto.builder()
                .raceName(raceName)
                .wager(wager)
                .startDate(start)
                .endDate(end)
                .fstUserId(id)
                .fstUserHabit(fstHabit)
                .build();

        Race entity = requestDto.toEntity();

        // fill the racer spots/vacancies
        entity.update(end, 321L, "some habit");

        Long raceId = raceRepository.save(entity).getId();

        String joinRaceUrl = "http://localhost:"+port+"/race/join/"+raceId;

        //when
        mvc.perform(get(joinRaceUrl)
                        .sessionAttr("user", new SessionUser(userEntity)))
        //then
                .andExpect(status().isForbidden());
    }
}
