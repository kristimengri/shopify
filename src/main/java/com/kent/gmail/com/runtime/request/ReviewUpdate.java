package com.kent.gmail.com.runtime.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kent.gmail.com.runtime.model.Review;
import com.kent.gmail.com.runtime.validation.IdValid;
import com.kent.gmail.com.runtime.validation.Update;

/** Object Used to Update Review */
@IdValid.List({
  @IdValid(
      field = "id",
      fieldType = Review.class,
      targetField = "review",
      groups = {Update.class})
})
public class ReviewUpdate extends ReviewCreate {

  private String id;

  @JsonIgnore private Review review;

  /**
   * @return id
   */
  public String getId() {
    return this.id;
  }

  /**
   * @param id id to set
   * @return ReviewUpdate
   */
  public <T extends ReviewUpdate> T setId(String id) {
    this.id = id;
    return (T) this;
  }

  /**
   * @return review
   */
  @JsonIgnore
  public Review getReview() {
    return this.review;
  }

  /**
   * @param review review to set
   * @return ReviewUpdate
   */
  public <T extends ReviewUpdate> T setReview(Review review) {
    this.review = review;
    return (T) this;
  }
}
