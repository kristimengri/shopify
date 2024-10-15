package com.kent.gmail.com.runtime.controller;

import com.kent.gmail.com.runtime.AppInit;
import com.kent.gmail.com.runtime.model.Category;
import com.kent.gmail.com.runtime.model.Product;
import com.kent.gmail.com.runtime.request.CategoryCreate;
import com.kent.gmail.com.runtime.request.CategoryFilter;
import com.kent.gmail.com.runtime.request.CategoryUpdate;
import com.kent.gmail.com.runtime.request.LoginRequest;
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
public class CategoryControllerTest {

  private Category testCategory;
  @Autowired private TestRestTemplate restTemplate;

  @Autowired private Product product;

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
  public void testCategoryCreate() {
    CategoryCreate request = new CategoryCreate();

    request.setProductId(this.product.getId());

    ResponseEntity<Category> response =
        this.restTemplate.postForEntity("/Category/createCategory", request, Category.class);
    Assertions.assertEquals(200, response.getStatusCodeValue());
    testCategory = response.getBody();
    assertCategory(request, testCategory);
  }

  @Test
  @Order(2)
  public void testListAllCategories() {
    CategoryFilter request = new CategoryFilter();
    ParameterizedTypeReference<PaginationResponse<Category>> t =
        new ParameterizedTypeReference<>() {};

    ResponseEntity<PaginationResponse<Category>> response =
        this.restTemplate.exchange(
            "/Category/getAllCategories", HttpMethod.POST, new HttpEntity<>(request), t);
    Assertions.assertEquals(200, response.getStatusCodeValue());
    PaginationResponse<Category> body = response.getBody();
    Assertions.assertNotNull(body);
    List<Category> Categories = body.getList();
    Assertions.assertNotEquals(0, Categories.size());
    Assertions.assertTrue(
        Categories.stream().anyMatch(f -> f.getId().equals(testCategory.getId())));
  }

  public void assertCategory(CategoryCreate request, Category testCategory) {
    Assertions.assertNotNull(testCategory);

    if (request.getProductId() != null) {

      Assertions.assertNotNull(testCategory.getProduct());
      Assertions.assertEquals(request.getProductId(), testCategory.getProduct().getId());
    }
  }

  @Test
  @Order(3)
  public void testCategoryUpdate() {
    CategoryUpdate request = new CategoryUpdate().setId(testCategory.getId());
    ResponseEntity<Category> response =
        this.restTemplate.exchange(
            "/Category/updateCategory", HttpMethod.PUT, new HttpEntity<>(request), Category.class);
    Assertions.assertEquals(200, response.getStatusCodeValue());
    testCategory = response.getBody();
    assertCategory(request, testCategory);
  }
}
