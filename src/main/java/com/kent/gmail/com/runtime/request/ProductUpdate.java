package com.kent.gmail.com.runtime.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kent.gmail.com.runtime.model.Product;
import com.kent.gmail.com.runtime.validation.IdValid;
import com.kent.gmail.com.runtime.validation.Update;

/** Object Used to Update Product */
@IdValid.List({
  @IdValid(
      field = "id",
      fieldType = Product.class,
      targetField = "product",
      groups = {Update.class})
})
public class ProductUpdate extends ProductCreate {

  private String id;

  @JsonIgnore private Product product;

  /**
   * @return id
   */
  public String getId() {
    return this.id;
  }

  /**
   * @param id id to set
   * @return ProductUpdate
   */
  public <T extends ProductUpdate> T setId(String id) {
    this.id = id;
    return (T) this;
  }

  /**
   * @return product
   */
  @JsonIgnore
  public Product getProduct() {
    return this.product;
  }

  /**
   * @param product product to set
   * @return ProductUpdate
   */
  public <T extends ProductUpdate> T setProduct(Product product) {
    this.product = product;
    return (T) this;
  }
}
