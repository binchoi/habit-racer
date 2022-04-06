package com.binchoi.springboot.config.auth;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

import java.io.Serializable;
import java.util.Locale;

public class CustomPermissionEvaluator implements PermissionEvaluator {

    @Override // e.g. hasPermission(PostsResponseDto, 'read') => not used that often / for api mostly
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        // functions no more than basic isAuthenticated() at the moment
        return (authentication != null) && (targetDomainObject != null) && permission instanceof String;
        // throw new UnsupportedOperationException();
    }

    @Override //e.g. hasPermission(2, 'Race', 'read'), hasPermission(2, 'Race', 'write'),
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        return (authentication != null) && (targetType != null) && permission instanceof String;
//        boolean res = false;
//        switch (targetType.toUpperCase(Locale.ROOT)) {
//            case "RACE":
//                res = true;
//            case "POST":
//                res = false;
//        }
    }
}
