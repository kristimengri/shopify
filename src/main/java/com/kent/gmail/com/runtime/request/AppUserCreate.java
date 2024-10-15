package com.kent.gmail.com.runtime.request;

/** Object Used to Create AppUser */
public class AppUserCreate {

  private String password;

  private String roles;

  private String username;

  /**
   * @return password
   */
  public String getPassword() {
    return this.password;
  }

  /**
   * @param password password to set
   * @return AppUserCreate
   */
  public <T extends AppUserCreate> T setPassword(String password) {
    this.password = password;
    return (T) this;
  }

  /**
   * @return roles
   */
  public String getRoles() {
    return this.roles;
  }

  /**
   * @param roles roles to set
   * @return AppUserCreate
   */
  public <T extends AppUserCreate> T setRoles(String roles) {
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
   * @return AppUserCreate
   */
  public <T extends AppUserCreate> T setUsername(String username) {
    this.username = username;
    return (T) this;
  }
}
