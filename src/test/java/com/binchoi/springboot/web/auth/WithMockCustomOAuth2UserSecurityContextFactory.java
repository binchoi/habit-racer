package com.binchoi.springboot.web.auth;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WithMockCustomOAuth2UserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomOAuth2User> {
    @Override
    public SecurityContext createSecurityContext(WithMockCustomOAuth2User customOAuth2User) {

        SecurityContext context = SecurityContextHolder.createEmptyContext();

        Map<String, Object> attributes = new HashMap<>();
        attributes.put("username", customOAuth2User.username());
        attributes.put("name", customOAuth2User.name());
        attributes.put("email", customOAuth2User.email());
        attributes.put("picture", customOAuth2User.picture());
        attributes.put("userId", Long.valueOf(customOAuth2User.userId()));

        OAuth2User principal = new DefaultOAuth2User(
                Stream.of(new OAuth2UserAuthority(customOAuth2User.role(), attributes)).collect(Collectors.toList()),
                attributes,
                "name");

        OAuth2AuthenticationToken token = new OAuth2AuthenticationToken(
                principal,
                principal.getAuthorities(),
                customOAuth2User.registrationId());

        context.setAuthentication(token);

        return context;
    }
}
