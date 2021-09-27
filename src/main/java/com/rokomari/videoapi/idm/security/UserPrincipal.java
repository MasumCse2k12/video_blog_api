package com.rokomari.videoapi.idm.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rokomari.videoapi.idm.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UserPrincipal implements UserDetails {
    private Integer id;
    private String name;
    private String userId;
    @JsonIgnore
    private String password;
    private Integer status;
    private String token;
    private Collection<? extends GrantedAuthority> authorities;

    public UserPrincipal(Integer id, String name, String userId,  Integer status, String token) {
        this.id = id;
        this.name = name;
        this.userId = userId;
        this.status = status;
        this.token = token;
    }

    public UserPrincipal(Integer id,  String name, String userId, String password,
                         Integer status, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.name = name;
        this.userId = userId;
        this.password = password;
        this.authorities = authorities;
        this.status = status;
    }


    public static UserPrincipal create(User user, List<GrantedAuthority> authorities ) {

        return new UserPrincipal(
                user.getId(),
                user.getName(),
                user.getUsername(),
                user.getPassword(),
                user.getStatus(),
                authorities
        );

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
