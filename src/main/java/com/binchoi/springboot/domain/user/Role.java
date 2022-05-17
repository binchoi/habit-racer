package com.binchoi.springboot.domain.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

    GUEST("ROLE_GUEST", "GUEST"),
    USER("ROLE_USER", "USER"),
    TESTER("ROLE_TESTER", "TESTER"),
    ADMIN("ROLE_ADMIN", "ADMIN");

    private final String key;
    private final String title;
    
}
