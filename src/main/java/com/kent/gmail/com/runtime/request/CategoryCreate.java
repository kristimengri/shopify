package com.kent.gmail.com.runtime.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kent.gmail.com.runtime.model.Product;
import com.kent.gmail.com.runtime.validation.Create;
import com.kent.gmail.com.runtime.validation.IdValid;
import com.kent.gmail.com.runtime.validation.Update;

/** Object Used to Create Category */
@IdValid.List({
  @IdValid(
      field = "productId",
      fieldType = Product.class,
      targetField = "product",
      groups = {Update.class, Create.class})
})
public class CategoryCreate extends BaseCreate {

  @JsonIgnore private Product product;

  private String productId;

  /**
   * @return product
   */
  @JsonIgnore
  public Product getProduct() {
    return this.product;
  }

  /**
   * @param product product to set
   * @return CategoryCreate
   */
  public <T extends CategoryCreate> T setProduct(Product product) {
    this.product = product;
    return (T) this;
  }

  /**
   * @return productId
   */
  public String getProductId() {
    return this.productId;
  }

  /**
   * @param productId productId to set
   * @return CategoryCreate
   */
  public <T extends CategoryCreate> T setProductId(String productId) {
    this.productId = productId;
    return (T) this;
  }
}
