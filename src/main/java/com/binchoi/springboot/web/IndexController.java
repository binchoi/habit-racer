package com.binchoi.springboot.web;

import com.binchoi.springboot.config.auth.LoginUser;
import com.binchoi.springboot.config.auth.dto.SessionUser;
import com.binchoi.springboot.service.posts.PostsService;
import com.binchoi.springboot.web.dto.PostsResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;

@RequiredArgsConstructor
@Controller
public class IndexController {

    private final PostsService postsService;
    private final HttpSession httpSession;

    @GetMapping("/")
    public String index(Model model, @LoginUser SessionUser user) {
        if (user!=null) {
            model.addAttribute("postsUser1", postsService.findByUserId(user.getId()));
            model.addAttribute("postsUser2", postsService.findByUserId(24L));
            model.addAttribute("userName", user.getName());
        }
        return "index";
    }

    @GetMapping("/posts/save")
    public String postsSave(Model model, @LoginUser SessionUser user) {
        model.addAttribute("userId", user.getId());
        model.addAttribute("today", LocalDate.now());
        return "posts-save";
    }

    @GetMapping("/posts/update/{id}")
    public String postsUpdate(@PathVariable Long id, Model model, @LoginUser SessionUser user) {
        PostsResponseDto dto = postsService.findById(id);
//        if (dto.getUserId()!=user.getId()) { - think about where is the best place for this check
//            return "index"; // or direct to page that says you are not allowed in
//        }
        model.addAttribute("post", dto);
        return "posts-update";
    }
}
