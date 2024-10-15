package com.kent.gmail.com.runtime.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class AppUser {

  @Id private String id;

  @JsonIgnore private String password;

  @JsonIgnore private String roles;

  private String username;

  /**
   * @return id
   */
  @Id
  public String getId() {
    return this.id;
  }

  /**
   * @param id id to set
   * @return AppUser
   */
  public <T extends AppUser> T setId(String id) {
    this.id = id;
    return (T) this;
  }

  /**
   * @return password
   */
  @JsonIgnore
  public String getPassword() {
    return this.password;
  }

  /**
   * @param password password to set
   * @return AppUser
   */
  public <T extends AppUser> T setPassword(String password) {
    this.password = password;
    return (T) this;
  }

  /**
   * @return roles
   */
  @JsonIgnore
  public String getRoles() {
    return this.roles;
  }

  /**
   * @param roles roles to set
   * @return AppUser
   */
  public <T extends AppUser> T setRoles(String roles) {
    this.roles = roles;
    return (T) this;
  }

  /**
   * @return username
   */
  public String getUsername() {
    return this.username;
  }

  /**
   * @param username username to set
   * @return AppUser
   */
  public <T extends AppUser> T setUsername(String username) {
    this.username = username;
    return (T) this;
  }
}
