package com.example.cacophony.security;

import com.example.cacophony.jooq.tables.records.CUserRecord;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class UserInfoDetails implements UserDetails {

    public String userName;
    public String password;
    public UUID userId;
    public List<SimpleGrantedAuthority> authorities;

    public UserInfoDetails(CUserRecord user) {
        this.userName = user.getUsername();
        this.password = user.getPassword();
        this.userId = user.getId();
        this.authorities = List.of(new SimpleGrantedAuthority(user.getUserRole()));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.userName;
    }
}
