package com.kent.gmail.com.runtime.controller;

import com.kent.gmail.com.runtime.AppInit;
import com.kent.gmail.com.runtime.model.Order;
import com.kent.gmail.com.runtime.request.LoginRequest;
import com.kent.gmail.com.runtime.request.OrderCreate;
import com.kent.gmail.com.runtime.request.OrderFilter;
import com.kent.gmail.com.runtime.request.OrderUpdate;
import com.kent.gmail.com.runtime.response.PaginationResponse;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.bind.annotation.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = AppInit.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
public class OrderControllerTest {

  private Order testOrder;
  @Autowired private TestRestTemplate restTemplate;

  @BeforeAll
  private void init() {
    ResponseEntity<Object> authenticationResponse =
        this.restTemplate.postForEntity(
            "/login",
            new LoginRequest().setUsername("admin@flexicore.com").setPassword("admin"),
            Object.class);
    String authenticationKey =
        authenticationResponse.getHeaders().get(HttpHeaders.AUTHORIZATION).stream()
            .findFirst()
            .orElse(null);
    restTemplate
        .getRestTemplate()
        .setInterceptors(
            Collections.singletonList(
                (request, body, execution) -> {
                  request.getHeaders().add("Authorization", "Bearer " + authenticationKey);
                  return execution.execute(request, body);
                }));
  }

  @Test
  @org.junit.jupiter.api.Order(1)
  public void testOrderCreate() {
    OrderCreate request = new OrderCreate();

    ResponseEntity<Order> response =
        this.restTemplate.postForEntity("/Order/createOrder", request, Order.class);
    Assertions.assertEquals(200, response.getStatusCodeValue());
    testOrder = response.getBody();
    assertOrder(request, testOrder);
  }

  @Test
  @org.junit.jupiter.api.Order(2)
  public void testListAllOrders() {
    OrderFilter request = new OrderFilter();
    ParameterizedTypeReference<PaginationResponse<Order>> t = new ParameterizedTypeReference<>() {};

    ResponseEntity<PaginationResponse<Order>> response =
        this.restTemplate.exchange(
            "/Order/getAllOrders", HttpMethod.POST, new HttpEntity<>(request), t);
    Assertions.assertEquals(200, response.getStatusCodeValue());
    PaginationResponse<Order> body = response.getBody();
    Assertions.assertNotNull(body);
    List<Order> Orders = body.getList();
    Assertions.assertNotEquals(0, Orders.size());
    Assertions.assertTrue(Orders.stream().anyMatch(f -> f.getId().equals(testOrder.getId())));
  }

  public void assertOrder(OrderCreate request, Order testOrder) {
    Assertions.assertNotNull(testOrder);
  }

  @Test
  @org.junit.jupiter.api.Order(3)
  public void testOrderUpdate() {
    OrderUpdate request = new OrderUpdate().setId(testOrder.getId());
    ResponseEntity<Order> response =
        this.restTemplate.exchange(
            "/Order/updateOrder", HttpMethod.PUT, new HttpEntity<>(request), Order.class);
    Assertions.assertEquals(200, response.getStatusCodeValue());
    testOrder = response.getBody();
    assertOrder(request, testOrder);
  }
}
