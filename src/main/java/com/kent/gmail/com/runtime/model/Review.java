package com.kent.gmail.com.runtime.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import java.util.List;

@Entity
public class Review extends Base {

  @OneToMany(targetEntity = Product.class, mappedBy = "review")
  @JsonIgnore
  private List<Product> reviewProducts;

  /**
   * @return reviewProducts
   */
  @OneToMany(targetEntity = Product.class, mappedBy = "review")
  @JsonIgnore
  public List<Product> getReviewProducts() {
    return this.reviewProducts;
  }

  /**
   * @param reviewProducts reviewProducts to set
   * @return Review
   */
  public <T extends Review> T setReviewProducts(List<Product> reviewProducts) {
    this.reviewProducts = reviewProducts;
    return (T) this;
  }
}
