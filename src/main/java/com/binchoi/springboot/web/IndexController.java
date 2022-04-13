package com.binchoi.springboot.web;

import com.binchoi.springboot.config.auth.LoginUser;
import com.binchoi.springboot.config.auth.dto.SessionUser;
import com.binchoi.springboot.domain.race.Race;
import com.binchoi.springboot.service.posts.PostsService;
import com.binchoi.springboot.service.race.RaceService;
import com.binchoi.springboot.service.user.UserService;
import com.binchoi.springboot.web.dto.PostsResponseDto;
import com.binchoi.springboot.web.dto.RaceListResponseDto;
import com.binchoi.springboot.web.dto.RaceResponseDto;
import com.binchoi.springboot.web.dto.RaceSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.ArrayList;

@RequiredArgsConstructor
@Controller
public class IndexController {

    private final PostsService postsService;
    private final RaceService raceService;
    private final UserService userService;
    private final HttpSession httpSession;

    @GetMapping("/")
    public String index(Model model, @LoginUser SessionUser user) {
        if (user!=null) {
            model.addAttribute("raceList", raceService.findByUserId(user.getId()));
            model.addAttribute("userName", user.getName());
        }
        return "index";
    }

    @PreAuthorize("hasPermission(#id, 'race', 'read')")
    @GetMapping("/race/{id}")
    public String raceView(@PathVariable Long id, Model model, @LoginUser SessionUser user) {
        RaceResponseDto race = raceService.findById(id); // throws exception if race does not exist
        Long fstUserId = race.getFstUserId();
        Long sndUserId = race.getSndUserId();
        boolean sndUserExists = sndUserId!=null;

        model.addAttribute("userName", user.getName());
        model.addAttribute("race", race);
        model.addAttribute("fstUserName", userService.findById(fstUserId).getName());
        model.addAttribute("sndUserName", sndUserExists ? userService.findById(sndUserId).getName() : "???");
        model.addAttribute("postsUser1", postsService.findByUserIdRaceId(fstUserId, id));
        model.addAttribute("postsUser2", sndUserExists ? postsService.findByUserIdRaceId(sndUserId, id) : new ArrayList<>());
        model.addAttribute("sndHabit", sndUserExists ? race.getSndUserHabit() : "???");

        return "race-overview";
    }

    @GetMapping("/race/save")
    public String raceSave(Model model, @LoginUser SessionUser user) {
        model.addAttribute("fstUserId", user.getId());
        model.addAttribute("today", LocalDate.now());
        model.addAttribute("nextMonth", LocalDate.now().plusMonths(1));
        return "race-save";
    }

    @GetMapping("/race/join")
    public String raceUpdate(Model model, @LoginUser SessionUser user) {
        model.addAttribute("sndUserId", user.getId());
        return "race-join-1";
    }

    @GetMapping("/race/join/{id}")
    public String raceUpdate(Model model, @LoginUser SessionUser user, @PathVariable Long id) {
        RaceResponseDto race = raceService.findById(id);
        String competitor = userService.findById(race.getFstUserId()).getName();
        model.addAttribute("race", race);
        model.addAttribute("competitor", competitor);
        model.addAttribute("sndUserId", user.getId());
        return "race-join-2";
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

    @PreAuthorize("hasPermission(#id, 'posts', 'write')")
    @GetMapping("posts/update/{id}")
    public String postsUpdate(@PathVariable Long id, Model model, @LoginUser SessionUser user) {
        PostsResponseDto dto = postsService.findById(id);
        if (!dto.getUserId().equals(user.getId())) return "forbidden-page";
        model.addAttribute("post", dto);
        model.addAttribute("raceId", postsService.findById(id).getRaceId());
        return "posts-update";
    }


}
