package com.kent.gmail.com.runtime.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kent.gmail.com.runtime.model.Category;
import com.kent.gmail.com.runtime.validation.IdValid;
import com.kent.gmail.com.runtime.validation.Update;

/** Object Used to Update Category */
@IdValid.List({
  @IdValid(
      field = "id",
      fieldType = Category.class,
      targetField = "category",
      groups = {Update.class})
})
public class CategoryUpdate extends CategoryCreate {

  @JsonIgnore private Category category;

  private String id;

  /**
   * @return category
   */
  @JsonIgnore
  public Category getCategory() {
    return this.category;
  }

  /**
   * @param category category to set
   * @return CategoryUpdate
   */
  public <T extends CategoryUpdate> T setCategory(Category category) {
    this.category = category;
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
   * @return CategoryUpdate
   */
  public <T extends CategoryUpdate> T setId(String id) {
    this.id = id;
    return (T) this;
  }
}
