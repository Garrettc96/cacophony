package com.example.cacophony.security;

import com.example.cacophony.data.UserRole;
import com.example.cacophony.data.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.Collection;
import java.util.List;

public class UserInfoDetails implements UserDetails {

  public String userName;
  public String password;
  public List<SimpleGrantedAuthority> authorities;

  public UserInfoDetails(User user) {
    this.userName = user.getUsername();
    this.password = user.getPassword();
    this.authorities = user.getRoles().stream().map((UserRole role) -> new SimpleGrantedAuthority(role.toString())).toList();
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
