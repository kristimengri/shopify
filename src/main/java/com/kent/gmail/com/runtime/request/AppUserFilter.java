package com.kent.gmail.com.runtime.request;

import jakarta.validation.constraints.Min;
import java.util.Set;

/** Object Used to List AppUser */
public class AppUserFilter {

  @Min(value = 0)
  private Integer currentPage;

  private Set<String> id;

  @Min(value = 1)
  private Integer pageSize;

  private Set<String> roles;

  private Set<String> username;

  /**
   * @return currentPage
   */
  public Integer getCurrentPage() {
    return this.currentPage;
  }

  /**
   * @param currentPage currentPage to set
   * @return AppUserFilter
   */
  public <T extends AppUserFilter> T setCurrentPage(Integer currentPage) {
    this.currentPage = currentPage;
    return (T) this;
  }

  /**
   * @return id
   */
  public Set<String> getId() {
    return this.id;
  }

  /**
   * @param id id to set
   * @return AppUserFilter
   */
  public <T extends AppUserFilter> T setId(Set<String> id) {
    this.id = id;
    return (T) this;
  }

  /**
   * @return pageSize
   */
  public Integer getPageSize() {
    return this.pageSize;
  }

  /**
   * @param pageSize pageSize to set
   * @return AppUserFilter
   */
  public <T extends AppUserFilter> T setPageSize(Integer pageSize) {
    this.pageSize = pageSize;
    return (T) this;
  }

  /**
   * @return roles
   */
  public Set<String> getRoles() {
    return this.roles;
  }

  /**
   * @param roles roles to set
   * @return AppUserFilter
   */
  public <T extends AppUserFilter> T setRoles(Set<String> roles) {
    this.roles = roles;
    return (T) this;
  }

  /**
   * @return username
   */
  public Set<String> getUsername() {
    return this.username;
  }

  /**
   * @param username username to set
   * @return AppUserFilter
   */
  public <T extends AppUserFilter> T setUsername(Set<String> username) {
    this.username = username;
    return (T) this;
  }
}
