package com.kent.gmail.com.runtime.controller;

import com.kent.gmail.com.runtime.model.Order;
import com.kent.gmail.com.runtime.request.OrderCreate;
import com.kent.gmail.com.runtime.request.OrderFilter;
import com.kent.gmail.com.runtime.request.OrderUpdate;
import com.kent.gmail.com.runtime.response.PaginationResponse;
import com.kent.gmail.com.runtime.security.UserSecurityContext;
import com.kent.gmail.com.runtime.service.OrderService;
import com.kent.gmail.com.runtime.validation.Create;
import com.kent.gmail.com.runtime.validation.Update;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("Order")
@Tag(name = "Order")
public class OrderController {

  @Autowired private OrderService orderService;

  @PostMapping("createOrder")
  @Operation(summary = "createOrder", description = "Creates Order")
  public Order createOrder(
      @Validated(Create.class) @RequestBody OrderCreate orderCreate,
      Authentication authentication) {

    UserSecurityContext securityContext = (UserSecurityContext) authentication.getPrincipal();

    return orderService.createOrder(orderCreate, securityContext);
  }

  @DeleteMapping("{id}")
  @Operation(summary = "deleteOrder", description = "Deletes Order")
  public Order deleteOrder(@PathVariable("id") String id, Authentication authentication) {

    UserSecurityContext securityContext = (UserSecurityContext) authentication.getPrincipal();

    return orderService.deleteOrder(id, securityContext);
  }

  @PostMapping("getAllOrders")
  @Operation(summary = "getAllOrders", description = "lists Orders")
  public PaginationResponse<Order> getAllOrders(
      @Valid @RequestBody OrderFilter orderFilter, Authentication authentication) {

    UserSecurityContext securityContext = (UserSecurityContext) authentication.getPrincipal();

    return orderService.getAllOrders(orderFilter, securityContext);
  }

  @PutMapping("updateOrder")
  @Operation(summary = "updateOrder", description = "Updates Order")
  public Order updateOrder(
      @Validated(Update.class) @RequestBody OrderUpdate orderUpdate,
      Authentication authentication) {

    UserSecurityContext securityContext = (UserSecurityContext) authentication.getPrincipal();

    return orderService.updateOrder(orderUpdate, securityContext);
  }
}
