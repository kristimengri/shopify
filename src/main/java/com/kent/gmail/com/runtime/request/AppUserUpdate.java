package com.kent.gmail.com.runtime.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kent.gmail.com.runtime.model.AppUser;
import com.kent.gmail.com.runtime.validation.IdValid;
import com.kent.gmail.com.runtime.validation.Update;

/** Object Used to Update AppUser */
@IdValid.List({
  @IdValid(
      field = "id",
      fieldType = AppUser.class,
      targetField = "appUser",
      groups = {Update.class})
})
public class AppUserUpdate extends AppUserCreate {

  @JsonIgnore private AppUser appUser;

  private String id;

  /**
   * @return appUser
   */
  @JsonIgnore
  public AppUser getAppUser() {
    return this.appUser;
  }

  /**
   * @param appUser appUser to set
   * @return AppUserUpdate
   */
  public <T extends AppUserUpdate> T setAppUser(AppUser appUser) {
    this.appUser = appUser;
    return (T) this;
  }

  /**
   * @return id
   */
  public String getId() {
    return this.id;
  }

  /**
   * @param id id to set
   * @return AppUserUpdate
   */
  public <T extends AppUserUpdate> T setId(String id) {
    this.id = id;
    return (T) this;
  }
}
