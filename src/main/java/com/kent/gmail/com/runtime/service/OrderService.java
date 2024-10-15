package com.kent.gmail.com.runtime.service;

import com.kent.gmail.com.runtime.data.OrderRepository;
import com.kent.gmail.com.runtime.model.Order;
import com.kent.gmail.com.runtime.model.Order_;
import com.kent.gmail.com.runtime.request.OrderCreate;
import com.kent.gmail.com.runtime.request.OrderFilter;
import com.kent.gmail.com.runtime.request.OrderUpdate;
import com.kent.gmail.com.runtime.response.PaginationResponse;
import com.kent.gmail.com.runtime.security.UserSecurityContext;
import jakarta.persistence.metamodel.SingularAttribute;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class OrderService {

  @Autowired private OrderRepository repository;

  @Autowired private BaseService baseService;

  /**
   * @param orderCreate Object Used to Create Order
   * @param securityContext
   * @return created Order
   */
  public Order createOrder(OrderCreate orderCreate, UserSecurityContext securityContext) {
    Order order = createOrderNoMerge(orderCreate, securityContext);
    order = this.repository.merge(order);
    return order;
  }

  /**
   * @param orderCreate Object Used to Create Order
   * @param securityContext
   * @return created Order unmerged
   */
  public Order createOrderNoMerge(OrderCreate orderCreate, UserSecurityContext securityContext) {
    Order order = new Order();
    order.setId(UUID.randomUUID().toString());
    updateOrderNoMerge(order, orderCreate);

    return order;
  }

  /**
   * @param orderCreate Object Used to Create Order
   * @param order
   * @return if order was updated
   */
  public boolean updateOrderNoMerge(Order order, OrderCreate orderCreate) {
    boolean update = baseService.updateBaseNoMerge(order, orderCreate);

    return update;
  }

  /**
   * @param orderUpdate
   * @param securityContext
   * @return order
   */
  public Order updateOrder(OrderUpdate orderUpdate, UserSecurityContext securityContext) {
    Order order = orderUpdate.getOrder();
    if (updateOrderNoMerge(order, orderUpdate)) {
      order = this.repository.merge(order);
    }
    return order;
  }

  /**
   * @param orderFilter Object Used to List Order
   * @param securityContext
   * @return PaginationResponse<Order> containing paging information for Order
   */
  public PaginationResponse<Order> getAllOrders(
      OrderFilter orderFilter, UserSecurityContext securityContext) {
    List<Order> list = listAllOrders(orderFilter, securityContext);
    long count = this.repository.countAllOrders(orderFilter, securityContext);
    return new PaginationResponse<>(list, orderFilter.getPageSize(), count);
  }

  /**
   * @param orderFilter Object Used to List Order
   * @param securityContext
   * @return List of Order
   */
  public List<Order> listAllOrders(OrderFilter orderFilter, UserSecurityContext securityContext) {
    return this.repository.listAllOrders(orderFilter, securityContext);
  }

  public Order deleteOrder(String id, UserSecurityContext securityContext) {
    Order order = this.repository.getByIdOrNull(Order.class, Order_.id, id, securityContext);
    ;
    if (order == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order not found");
    }

    this.repository.remove(order);

    return order;
  }

  public <T extends Order, I> List<T> listByIds(
      Class<T> c,
      SingularAttribute<? super T, I> idField,
      Set<I> ids,
      UserSecurityContext securityContext) {
    return repository.listByIds(c, idField, ids, securityContext);
  }

  public <T extends Order, I> T getByIdOrNull(
      Class<T> c,
      SingularAttribute<? super T, I> idField,
      I id,
      UserSecurityContext securityContext) {
    return repository.getByIdOrNull(c, idField, id, securityContext);
  }

  public <T extends Order, I> T getByIdOrNull(
      Class<T> c, SingularAttribute<? super T, I> idField, I id) {
    return repository.getByIdOrNull(c, idField, id);
  }

  public <T extends Order, I> List<T> listByIds(
      Class<T> c, SingularAttribute<? super T, I> idField, Set<I> ids) {
    return repository.listByIds(c, idField, ids);
  }

  public <T> T merge(T base) {
    return this.repository.merge(base);
  }

  public void massMerge(List<?> toMerge) {
    this.repository.massMerge(toMerge);
  }
}
