package com.kent.gmail.com.runtime.controller;

import com.kent.gmail.com.runtime.AppInit;
import com.kent.gmail.com.runtime.model.Product;
import com.kent.gmail.com.runtime.model.Review;
import com.kent.gmail.com.runtime.request.LoginRequest;
import com.kent.gmail.com.runtime.request.ProductCreate;
import com.kent.gmail.com.runtime.request.ProductFilter;
import com.kent.gmail.com.runtime.request.ProductUpdate;
import com.kent.gmail.com.runtime.response.PaginationResponse;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
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
public class ProductControllerTest {

  private Product testProduct;
  @Autowired private TestRestTemplate restTemplate;

  @Autowired private Review review;

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
  @Order(1)
  public void testProductCreate() {
    ProductCreate request = new ProductCreate();

    request.setPrice(10D);

    request.setReviewId(this.review.getId());

    ResponseEntity<Product> response =
        this.restTemplate.postForEntity("/Product/createProduct", request, Product.class);
    Assertions.assertEquals(200, response.getStatusCodeValue());
    testProduct = response.getBody();
    assertProduct(request, testProduct);
  }

  @Test
  @Order(2)
  public void testListAllProducts() {
    ProductFilter request = new ProductFilter();
    ParameterizedTypeReference<PaginationResponse<Product>> t =
        new ParameterizedTypeReference<>() {};

    ResponseEntity<PaginationResponse<Product>> response =
        this.restTemplate.exchange(
            "/Product/getAllProducts", HttpMethod.POST, new HttpEntity<>(request), t);
    Assertions.assertEquals(200, response.getStatusCodeValue());
    PaginationResponse<Product> body = response.getBody();
    Assertions.assertNotNull(body);
    List<Product> Products = body.getList();
    Assertions.assertNotEquals(0, Products.size());
    Assertions.assertTrue(Products.stream().anyMatch(f -> f.getId().equals(testProduct.getId())));
  }

  public void assertProduct(ProductCreate request, Product testProduct) {
    Assertions.assertNotNull(testProduct);

    if (request.getPrice() != null) {
      Assertions.assertEquals(request.getPrice(), testProduct.getPrice());
    }

    if (request.getReviewId() != null) {

      Assertions.assertNotNull(testProduct.getReview());
      Assertions.assertEquals(request.getReviewId(), testProduct.getReview().getId());
    }
  }

  @Test
  @Order(3)
  public void testProductUpdate() {
    ProductUpdate request = new ProductUpdate().setId(testProduct.getId());
    ResponseEntity<Product> response =
        this.restTemplate.exchange(
            "/Product/updateProduct", HttpMethod.PUT, new HttpEntity<>(request), Product.class);
    Assertions.assertEquals(200, response.getStatusCodeValue());
    testProduct = response.getBody();
    assertProduct(request, testProduct);
  }
}
