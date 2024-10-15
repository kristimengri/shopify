package com.kent.gmail.com.runtime.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kent.gmail.com.runtime.model.Review;
import com.kent.gmail.com.runtime.validation.Create;
import com.kent.gmail.com.runtime.validation.IdValid;
import com.kent.gmail.com.runtime.validation.Update;

/** Object Used to Create Product */
@IdValid.List({
  @IdValid(
      field = "reviewId",
      fieldType = Review.class,
      targetField = "review",
      groups = {Update.class, Create.class})
})
public class ProductCreate extends BaseCreate {

  private Double price;

  @JsonIgnore private Review review;

  private String reviewId;

  /**
   * @return price
   */
  public Double getPrice() {
    return this.price;
  }

  /**
   * @param price price to set
   * @return ProductCreate
   */
  public <T extends ProductCreate> T setPrice(Double price) {
    this.price = price;
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
   * @return ProductCreate
   */
  public <T extends ProductCreate> T setReview(Review review) {
    this.review = review;
    return (T) this;
  }

  /**
   * @return reviewId
   */
  public String getReviewId() {
    return this.reviewId;
  }

  /**
   * @param reviewId reviewId to set
   * @return ProductCreate
   */
  public <T extends ProductCreate> T setReviewId(String reviewId) {
    this.reviewId = reviewId;
    return (T) this;
  }
}
