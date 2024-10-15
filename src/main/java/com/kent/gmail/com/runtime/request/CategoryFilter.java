package com.kent.gmail.com.runtime.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kent.gmail.com.runtime.model.Product;
import com.kent.gmail.com.runtime.validation.IdValid;
import java.util.List;
import java.util.Set;

/** Object Used to List Category */
@IdValid.List({@IdValid(field = "productIds", fieldType = Product.class, targetField = "products")})
public class CategoryFilter extends BaseFilter {

  private Set<String> productIds;

  @JsonIgnore private List<Product> products;

  /**
   * @return productIds
   */
  public Set<String> getProductIds() {
    return this.productIds;
  }

  /**
   * @param productIds productIds to set
   * @return CategoryFilter
   */
  public <T extends CategoryFilter> T setProductIds(Set<String> productIds) {
    this.productIds = productIds;
    return (T) this;
  }

  /**
   * @return products
   */
  @JsonIgnore
  public List<Product> getProducts() {
    return this.products;
  }

  /**
   * @param products products to set
   * @return CategoryFilter
   */
  public <T extends CategoryFilter> T setProducts(List<Product> products) {
    this.products = products;
    return (T) this;
  }
}
