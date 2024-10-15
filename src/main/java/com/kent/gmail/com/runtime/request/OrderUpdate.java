package com.kent.gmail.com.runtime.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kent.gmail.com.runtime.model.Order;
import com.kent.gmail.com.runtime.validation.IdValid;
import com.kent.gmail.com.runtime.validation.Update;

/** Object Used to Update Order */
@IdValid.List({
  @IdValid(
      field = "id",
      fieldType = Order.class,
      targetField = "order",
      groups = {Update.class})
})
public class OrderUpdate extends OrderCreate {

  private String id;

  @JsonIgnore private Order order;

  /**
   * @return id
   */
  public String getId() {
    return this.id;
  }

  /**
   * @param id id to set
   * @return OrderUpdate
   */
  public <T extends OrderUpdate> T setId(String id) {
    this.id = id;
    return (T) this;
  }

  /**
   * @return order
   */
  @JsonIgnore
  public Order getOrder() {
    return this.order;
  }

  /**
   * @param order order to set
   * @return OrderUpdate
   */
  public <T extends OrderUpdate> T setOrder(Order order) {
    this.order = order;
    return (T) this;
  }
}
