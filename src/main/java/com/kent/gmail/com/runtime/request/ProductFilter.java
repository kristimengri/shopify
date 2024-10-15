package com.kent.gmail.com.runtime.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kent.gmail.com.runtime.model.Category;
import com.kent.gmail.com.runtime.model.Review;
import com.kent.gmail.com.runtime.validation.IdValid;
import java.util.List;
import java.util.Set;

/** Object Used to List Product */
@IdValid.List({
  @IdValid(field = "reviewIds", fieldType = Review.class, targetField = "reviews"),
  @IdValid(
      field = "productCategoriesIds",
      fieldType = Category.class,
      targetField = "productCategorieses")
})
public class ProductFilter extends BaseFilter {

  private Double priceEnd;

  private Double priceStart;

  private Set<String> productCategoriesIds;

  @JsonIgnore private List<Category> productCategorieses;

  private Set<String> reviewIds;

  @JsonIgnore private List<Review> reviews;

  /**
   * @return priceEnd
   */
  public Double getPriceEnd() {
    return this.priceEnd;
  }

  /**
   * @param priceEnd priceEnd to set
   * @return ProductFilter
   */
  public <T extends ProductFilter> T setPriceEnd(Double priceEnd) {
    this.priceEnd = priceEnd;
    return (T) this;
  }

  /**
   * @return priceStart
   */
  public Double getPriceStart() {
    return this.priceStart;
  }

  /**
   * @param priceStart priceStart to set
   * @return ProductFilter
   */
  public <T extends ProductFilter> T setPriceStart(Double priceStart) {
    this.priceStart = priceStart;
    return (T) this;
  }

  /**
   * @return productCategoriesIds
   */
  public Set<String> getProductCategoriesIds() {
    return this.productCategoriesIds;
  }

  /**
   * @param productCategoriesIds productCategoriesIds to set
   * @return ProductFilter
   */
  public <T extends ProductFilter> T setProductCategoriesIds(Set<String> productCategoriesIds) {
    this.productCategoriesIds = productCategoriesIds;
    return (T) this;
  }

  /**
   * @return productCategorieses
   */
  @JsonIgnore
  public List<Category> getProductCategorieses() {
    return this.productCategorieses;
  }

  /**
   * @param productCategorieses productCategorieses to set
   * @return ProductFilter
   */
  public <T extends ProductFilter> T setProductCategorieses(List<Category> productCategorieses) {
    this.productCategorieses = productCategorieses;
    return (T) this;
  }

  /**
   * @return reviewIds
   */
  public Set<String> getReviewIds() {
    return this.reviewIds;
  }

  /**
   * @param reviewIds reviewIds to set
   * @return ProductFilter
   */
  public <T extends ProductFilter> T setReviewIds(Set<String> reviewIds) {
    this.reviewIds = reviewIds;
    return (T) this;
  }

  /**
   * @return reviews
   */
  @JsonIgnore
  public List<Review> getReviews() {
    return this.reviews;
  }

  /**
   * @param reviews reviews to set
   * @return ProductFilter
   */
  public <T extends ProductFilter> T setReviews(List<Review> reviews) {
    this.reviews = reviews;
    return (T) this;
  }
}
