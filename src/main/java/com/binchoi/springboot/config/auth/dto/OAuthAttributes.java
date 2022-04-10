package com.binchoi.springboot.config.auth.dto;

import com.binchoi.springboot.domain.user.Role;
import com.binchoi.springboot.domain.user.User;
import lombok.Builder;
import lombok.Getter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Getter
public class OAuthAttributes {
    private Map<String,Object> attributes;
    private String nameAttributeKey;
    private String name;
    private String email;
    private String picture;

    @Builder
    public OAuthAttributes(Map<String,Object> attributes, String nameAttributeKey, String name, String email, String picture) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
        this.picture = picture;
    }

    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String,Object> attributes) {
        return ofGoogle(userNameAttributeName,attributes);
    }

    private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String,Object> attributes) {
        return OAuthAttributes.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .picture((String) attributes.get("picture"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    public User toEntity() {
        return User.builder()
                .name(name)
                .email(email)
                .picture(picture)
                .role(Role.USER) // why guest why user why - think
                .build();
    }

    /**
     * Adds UserId key-value pair to attributes Map of DefaultOAuth2User. This allows direct access to userId
     * via authentication.principal.attributes (to be confirmed) without needing to send a query to userRepository
     */
    public void putUserIdInAttributes(Long id) {
        // create modifiable copy of attributes map and append userId
        Map<String,Object> modifiableMap = new HashMap<>(attributes);
        modifiableMap.put("userId", id);
        // reassign attributes to new unmodifiableMap that contains userId kv-pair
        attributes = Collections.unmodifiableMap(modifiableMap);
    }

}
