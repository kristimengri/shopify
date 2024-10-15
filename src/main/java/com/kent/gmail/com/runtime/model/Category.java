package com.kent.gmail.com.runtime.model;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

@Entity
public class Category extends Base {

  @ManyToOne(targetEntity = Product.class)
  private Product product;

  /**
   * @return product
   */
  @ManyToOne(targetEntity = Product.class)
  public Product getProduct() {
    return this.product;
  }

  /**
   * @param product product to set
   * @return Category
   */
  public <T extends Category> T setProduct(Product product) {
    this.product = product;
    return (T) this;
  }
}
