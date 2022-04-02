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

    @GetMapping("/race/{id}")
    public String raceView(@PathVariable Long id, Model model, @LoginUser SessionUser user) {
        //check if this race is viewable by the currently logged in user - using token
        //no need to check if user exists because this uri is not approachable by non-authenticated users
        model.addAttribute("userName", user.getName());

        RaceResponseDto race = raceService.findById(id);
        model.addAttribute("race", race);

        //make clean code later
        Long fstUserId = race.getFstUserId(); // we don't use user.getId() because the user might be different from the race competitors (e.g. admin
        Long sndUserId = race.getSndUserId();
        model.addAttribute("fstUserName", userService.findById(fstUserId).getName());
        model.addAttribute("postsUser1", postsService.findByUserIdRaceId(fstUserId, id));

        if (sndUserId!=null) {
            model.addAttribute("sndUserName", userService.findById(sndUserId).getName());
            model.addAttribute("sndHabit", race.getSndUserHabit());
            model.addAttribute("postsUser2", postsService.findByUserIdRaceId(sndUserId, id));
        } else {
            model.addAttribute("sndUserName", "???");
            model.addAttribute("sndHabit", "???");
            model.addAttribute("postsUser2", new ArrayList<>());
        }
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
    public String raceUpdate(Model model) {
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

    @GetMapping("race/{id}/posts/save")
    public String postsSave(Model model, @LoginUser SessionUser user, @PathVariable Long id) {
        model.addAttribute("userId", user.getId());
        model.addAttribute("raceId", id);
        model.addAttribute("today", LocalDate.now());
        return "posts-save";
    }

    @GetMapping("posts/update/{id}") // including raceId ensures that less posts accessible by malice?
    public String postsUpdate(@PathVariable Long id, Model model, @LoginUser SessionUser user) {
        PostsResponseDto dto = postsService.findById(id);
        model.addAttribute("post", dto);
        model.addAttribute("raceId", postsService.findById(id).getRaceId());
        return "posts-update";
    }


}
