package com.eventx.api.security;

import com.eventx.api.models.User;
import java.util.Collection;
import java.util.List;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserPrincipal implements UserDetails {

    @Getter
    private final User user;
    private final String admins;

    public UserPrincipal(User user, String admins) {
        this.user = user;
        this.admins = admins != null ? admins : "";
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (admins.contains(user.getUsername())) {
            return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }
        return List.of();
    }

    @Override
    public String getPassword() {
        return user.getPasswordHash();
    }

    @Override
    public String getUsername() {
        return user.getId();
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
