package com.binchoi.springboot.web.auth;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomOAuth2UserSecurityContextFactory.class)
public @interface WithMockCustomOAuth2User {

    String username() default "username";
    String name() default "name";
    String email() default "test@default.email";
    String picture() default  "https://get_my_picture.com";
    String role() default "ROLE_USER";
    String registrationId() default "google";
    String userId() default "1";
}
