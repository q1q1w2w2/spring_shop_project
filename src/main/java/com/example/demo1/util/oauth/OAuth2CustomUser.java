package com.example.demo1.util.oauth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@ToString
public class OAuth2CustomUser implements OAuth2User, Serializable {

    private String registrationId;
    private Map<String, Object> attributes;
    private List<GrantedAuthority> authorities;

    @Getter
    private String email;

    @Override
    public Map<String, Object> getAttributes() {
        return this.attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getName() {
        return this.registrationId;
    }

}
