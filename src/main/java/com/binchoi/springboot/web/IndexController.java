package com.binchoi.springboot.web;

import com.binchoi.springboot.config.auth.LoginUser;
import com.binchoi.springboot.config.auth.dto.SessionUser;
import com.binchoi.springboot.domain.race.Race;
import com.binchoi.springboot.service.posts.PostsService;
import com.binchoi.springboot.service.race.RaceService;
import com.binchoi.springboot.service.user.UserService;
import com.binchoi.springboot.web.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Controller
public class IndexController {

    private final PostsService postsService;
    private final RaceService raceService;
    private final UserService userService;

    @GetMapping("/")
    public String index(Model model, @LoginUser SessionUser user) {
        if (user!=null) {
            model.addAttribute("user", user);
            Map<Boolean, List<RaceListResponseDto>> raceMap = raceService.findByUserId(user.getId());
            model.addAttribute("ongoingRaceList", raceMap.get(Boolean.FALSE));
            model.addAttribute("completeRaceList", raceMap.get(Boolean.TRUE));
            // (-) of mustache (no counterpart to 'inverse selection' = verbose)
            if (raceMap.get(Boolean.TRUE)==null) model.addAttribute("completedRacesFlag", 0);
        } else {
            model.addAttribute("completedRacesFlag", 0);
        }
        return "index";
    }

    @GetMapping("/race/sample")
    public String sampleRaceView() {
        return "sample-race-overview";
    }

    //admin can view race overview page
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasPermission(#id, 'race', 'read')")
    @GetMapping("/race/{id}")
    public String raceView(@PathVariable Long id, Model model, @LoginUser SessionUser user) {
        model.addAttribute("user", user);

        RaceResponseDto race = raceService.findById(id);
        RaceTimeInfoDto raceTimeInfo = new RaceTimeInfoDto(race);

        Long fstUserId = race.getFstUserId();
        Long sndUserId = race.getSndUserId();

        boolean sndUserJoined = sndUserId!=null;

        List<PostsListResponseDto> fstUserPosts = postsService.findByUserIdRaceId(fstUserId, id);
        Integer fstUserSuccessCount = fstUserPosts.size();
        Long fstUserSuccessPercent = fstUserSuccessCount*100 / raceTimeInfo.getDaysFromStart();
        List<PostsListResponseDto> sndUserPosts = sndUserJoined ? postsService.findByUserIdRaceId(sndUserId, id) : new ArrayList<>();
        Integer sndUserSuccessCount = sndUserPosts.size();
        Long sndUserSuccessPercent = sndUserSuccessCount*100 / raceTimeInfo.getDaysFromStart();

        model.addAttribute("fstUserName", userService.findById(fstUserId).getName());
        model.addAttribute("sndUserName", sndUserJoined ? userService.findById(sndUserId).getName() : "TBD");
        model.addAttribute("sndUserHabit", sndUserJoined ? race.getSndUserHabit() : "TBD");
        model.addAttribute("race", race);
        model.addAttribute("raceTimeInfo", raceTimeInfo);
        model.addAttribute("fstUserSuccessCount", fstUserSuccessCount);
        model.addAttribute("fstUserSuccessPercent", fstUserSuccessPercent);
        model.addAttribute("fstUserPosts", fstUserPosts);
        model.addAttribute("sndUserSuccessCount", sndUserSuccessCount);
        model.addAttribute("sndUserSuccessPercent", sndUserSuccessPercent);
        model.addAttribute("sndUserPosts", sndUserPosts);

        model.addAttribute("messageListResponseDto", postsService.findByRaceId(id));
        return "race-overview";
    }

    @GetMapping("/race/save")
    public String raceSave(Model model, @LoginUser SessionUser user) {
        model.addAttribute("fstUserId", user.getId());
        model.addAttribute("today", LocalDate.now());
        model.addAttribute("nextMonth", LocalDate.now().plusMonths(1));
        return "race-save";
    }

    @GetMapping("/race/update/{id}")
    public String raceUpdate(Model model, @LoginUser SessionUser user, @PathVariable Long id) {
        RaceResponseDto race = raceService.findById(id);
        String userTitle = String.format("you (%s)",user.getName());

        model.addAttribute("race", race);
        boolean isFstUser = user.getId().equals(race.getFstUserId());
        model.addAttribute("fstUserTitle", isFstUser ? userTitle : "your opponent");
        model.addAttribute("sndUserTitle", isFstUser ? "your opponent" : userTitle);
        model.addAttribute(isFstUser ? "isFstUser" : "isSndUser", 0);
        model.addAttribute("sndUserHabit", race.getSndUserId()==null ? "TBD" : race.getSndUserHabit());
        return "race-update";
    }

    @GetMapping("/race/join/{id}")
    public String raceJoin(Model model, @LoginUser SessionUser user, @PathVariable Long id) {
        RaceResponseDto race = raceService.findById(id);
        String competitor = userService.findById(race.getFstUserId()).getName();
        model.addAttribute("race", race);
        model.addAttribute("competitor", competitor);
        model.addAttribute("sndUserId", user.getId());
        return "race-join";
    }

    @PreAuthorize("hasPermission(#id, 'race', 'write')")
    @GetMapping("race/{id}/posts/save")
    public String postsSave(Model model, @LoginUser SessionUser user, @PathVariable Long id) {
        RaceResponseDto race = raceService.findById(id);
        model.addAttribute("userId", user.getId());
        model.addAttribute("raceId", id);
        model.addAttribute("today", LocalDate.now());
        return "posts-save";
    }

    // admin can update/delete posts
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasPermission(#id, 'posts', 'write')")
    @GetMapping("posts/update/{id}")
    public String postsUpdate(@PathVariable Long id, Model model, @LoginUser SessionUser user) {
        PostsResponseDto dto = postsService.findById(id);
        model.addAttribute("post", dto);
        model.addAttribute("raceId", postsService.findById(id).getRaceId());
        return "posts-update";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasPermission(#id, 'user', 'write')")
    @GetMapping("user/{id}")
    public String userView(@PathVariable Long id, Model model) {
        UserResponseDto user = userService.findById(id);
        Map<Boolean, List<RaceListResponseDto>> raceMap = raceService.findByUserId(id);
        model.addAttribute("user", user);
        model.addAttribute("userRaceInfoDto", new UserRaceInfoDto(id, raceMap));
//        model.addAttribute("ongoingRaceList", raceMap.get(Boolean.FALSE));
//        model.addAttribute("completeRaceList", raceMap.get(Boolean.TRUE));
        return "user-overview";
    }

//    private void summary(Map<Boolean, List<RaceListResponseDto>> raceMap) {
//
//    }


}
