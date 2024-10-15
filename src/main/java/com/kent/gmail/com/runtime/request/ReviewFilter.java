package com.kent.gmail.com.runtime.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kent.gmail.com.runtime.model.Product;
import com.kent.gmail.com.runtime.validation.IdValid;
import java.util.List;
import java.util.Set;

/** Object Used to List Review */
@IdValid.List({
  @IdValid(field = "reviewProductsIds", fieldType = Product.class, targetField = "reviewProductses")
})
public class ReviewFilter extends BaseFilter {

  private Set<String> reviewProductsIds;

  @JsonIgnore private List<Product> reviewProductses;

  /**
   * @return reviewProductsIds
   */
  public Set<String> getReviewProductsIds() {
    return this.reviewProductsIds;
  }

  /**
   * @param reviewProductsIds reviewProductsIds to set
   * @return ReviewFilter
   */
  public <T extends ReviewFilter> T setReviewProductsIds(Set<String> reviewProductsIds) {
    this.reviewProductsIds = reviewProductsIds;
    return (T) this;
  }

  /**
   * @return reviewProductses
   */
  @JsonIgnore
  public List<Product> getReviewProductses() {
    return this.reviewProductses;
  }

  /**
   * @param reviewProductses reviewProductses to set
   * @return ReviewFilter
   */
  public <T extends ReviewFilter> T setReviewProductses(List<Product> reviewProductses) {
    this.reviewProductses = reviewProductses;
    return (T) this;
  }
}
