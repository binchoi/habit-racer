package com.binchoi.springboot.config.auth;

import com.binchoi.springboot.service.posts.PostsService;
import com.binchoi.springboot.service.race.RaceService;
import com.binchoi.springboot.web.dto.RaceResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.io.Serializable;

@Configuration
@RequiredArgsConstructor
public class CustomPermissionEvaluator implements PermissionEvaluator {

    private final PostsService postsService;
    private final RaceService raceService;

    /**
     *
     * @param auth: represents the user in question. Should not be null. It contains variable attributes -
     *                      an unmodifiableMap which stores info about the operating user like userId, name, and email
     * @param targetId: identifier for the object instance (e.g. post_id, user_id, race_id, etc...) - usually a Long
     * @param targetType: a String representing the target's type (usually a Java classname). Not null.
     *                  (e.g. posts, race, user, etc...)
     * @param permission: a representation of the permission object as supplied by the expression system. Not null.
     * @return true if the permission is granted, false otherwise
     */
    @Override
    public boolean hasPermission(Authentication auth, Serializable targetId, String targetType, Object permission) {
        if ((auth == null) || targetType == null || !(permission instanceof String)) {
            return false;
        }
        DefaultOAuth2User oAuth2User  = (DefaultOAuth2User) auth.getPrincipal();
        Long userId = (Long) oAuth2User.getAttributes().get("userId");
        switch (targetType.toLowerCase()) {
            case "race":
                return hasRacePermission(userId, targetId, ((String) permission).toLowerCase());
            case "posts":
                return hasPostsPermission(userId, targetId, ((String) permission).toLowerCase());
            case "user":
                return hasUserPermission(userId, targetId, ((String) permission).toLowerCase());
            default:
                throw new UnsupportedOperationException("targetType is not supported");
        }
    }

    private boolean hasPostsPermission(Long userId, Serializable targetId, Object permission) {
        if (permission.equals("read")) {
            return true;
        } else if (permission.equals("write")) { // updating or deleting posts (only by author)
            return userId.equals(postsService.findById((Long) targetId).getUserId());
        } else {
            return false;
        }
    }

    // WRITE: "write a post in the race", update/delete race,
    // READ : "view the race overview & posts of a particular race with targetId"
    // JOIN :
    /**
     *
     * Race Permission Objects:
     * - WRITE: update / delete race; write post to race
     * - READ : view race overview page
     * - JOIN : join the race (i.e. display race-join page)
     *
     */
    private boolean hasRacePermission(Long userId, Serializable targetId, Object permission) {
        RaceResponseDto race = raceService.findById((Long) targetId);
        if (permission.equals("read") || permission.equals("write")) {
            return (userId.equals(race.getFstUserId()) || userId.equals(race.getSndUserId()));
        } else if (permission.equals("join")) {
            return (race.getSndUserId()==null && !userId.equals(race.getFstUserId()));
        } else {
            return false;
        }
    }

    private boolean hasUserPermission(Long userId, Serializable targetId, Object permission) {
        if (permission.equals("read") || permission.equals("write")) {
            return (userId.equals(targetId));
        } else {
            return false;
        }
    }

    /**
     * This method will not be the primary use of hasPermission() [not used]
     */
    @Override
    public boolean hasPermission(Authentication auth, Object targetDomainObject, Object permission) {
        // functions no more than basic isAuthenticated() at the moment
        return (auth != null) && (targetDomainObject != null) && permission instanceof String;
    }
}
