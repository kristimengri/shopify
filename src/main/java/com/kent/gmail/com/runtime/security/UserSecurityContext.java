package com.kent.gmail.com.runtime.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kent.gmail.com.runtime.model.AppUser;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserSecurityContext implements UserDetails {
  private final AppUser appUser;

  public UserSecurityContext(AppUser appUser) {
    this.appUser = appUser;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {

    return Optional.ofNullable(appUser.getRoles()).map(f -> f.split(",")).stream()
        .flatMap(Arrays::stream)
        .map(f -> new SimpleGrantedAuthority("ROLE_" + f.trim()))
        .toList();
  }

  @JsonIgnore
  @Override
  public String getPassword() {
    return appUser.getPassword();
  }

  @Override
  public String getUsername() {
    return appUser.getUsername();
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

  public String getId() {
    return appUser.getId();
  }

  public AppUser getUser() {
    return appUser;
  }

  public boolean isAdmin() {
    return false;
  }
}
