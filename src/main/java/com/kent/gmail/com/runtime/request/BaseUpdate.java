package com.kent.gmail.com.runtime.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kent.gmail.com.runtime.model.Base;
import com.kent.gmail.com.runtime.validation.IdValid;
import com.kent.gmail.com.runtime.validation.Update;

/** Object Used to Update Base */
@IdValid.List({
  @IdValid(
      field = "id",
      fieldType = Base.class,
      targetField = "base",
      groups = {Update.class})
})
public class BaseUpdate extends BaseCreate {

  @JsonIgnore private Base base;

  private String id;

  /**
   * @return base
   */
  @JsonIgnore
  public Base getBase() {
    return this.base;
  }

  /**
   * @param base base to set
   * @return BaseUpdate
   */
  public <T extends BaseUpdate> T setBase(Base base) {
    this.base = base;
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
   * @return BaseUpdate
   */
  public <T extends BaseUpdate> T setId(String id) {
    this.id = id;
    return (T) this;
  }
}
