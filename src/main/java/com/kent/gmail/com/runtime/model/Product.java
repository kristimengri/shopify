package com.kent.gmail.com.runtime.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.List;

@Entity
public class Product extends Base {

  private Double price;

  @OneToMany(targetEntity = Category.class, mappedBy = "product")
  @JsonIgnore
  private List<Category> productCategories;

  @ManyToOne(targetEntity = Review.class)
  private Review review;

  /**
   * @return price
   */
  public Double getPrice() {
    return this.price;
  }

  /**
   * @param price price to set
   * @return Product
   */
  public <T extends Product> T setPrice(Double price) {
    this.price = price;
    return (T) this;
  }

  /**
   * @return productCategories
   */
  @OneToMany(targetEntity = Category.class, mappedBy = "product")
  @JsonIgnore
  public List<Category> getProductCategories() {
    return this.productCategories;
  }

  /**
   * @param productCategories productCategories to set
   * @return Product
   */
  public <T extends Product> T setProductCategories(List<Category> productCategories) {
    this.productCategories = productCategories;
    return (T) this;
  }

  /**
   * @return review
   */
  @ManyToOne(targetEntity = Review.class)
  public Review getReview() {
    return this.review;
  }

  /**
   * @param review review to set
   * @return Product
   */
  public <T extends Product> T setReview(Review review) {
    this.review = review;
    return (T) this;
  }
}
