package com.kent.gmail.com.runtime.controller;

import com.kent.gmail.com.runtime.AppInit;
import com.kent.gmail.com.runtime.model.Review;
import com.kent.gmail.com.runtime.request.LoginRequest;
import com.kent.gmail.com.runtime.request.ReviewCreate;
import com.kent.gmail.com.runtime.request.ReviewFilter;
import com.kent.gmail.com.runtime.request.ReviewUpdate;
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
public class ReviewControllerTest {

  private Review testReview;
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
  @Order(1)
  public void testReviewCreate() {
    ReviewCreate request = new ReviewCreate();

    ResponseEntity<Review> response =
        this.restTemplate.postForEntity("/Review/createReview", request, Review.class);
    Assertions.assertEquals(200, response.getStatusCodeValue());
    testReview = response.getBody();
    assertReview(request, testReview);
  }

  @Test
  @Order(2)
  public void testListAllReviews() {
    ReviewFilter request = new ReviewFilter();
    ParameterizedTypeReference<PaginationResponse<Review>> t =
        new ParameterizedTypeReference<>() {};

    ResponseEntity<PaginationResponse<Review>> response =
        this.restTemplate.exchange(
            "/Review/getAllReviews", HttpMethod.POST, new HttpEntity<>(request), t);
    Assertions.assertEquals(200, response.getStatusCodeValue());
    PaginationResponse<Review> body = response.getBody();
    Assertions.assertNotNull(body);
    List<Review> Reviews = body.getList();
    Assertions.assertNotEquals(0, Reviews.size());
    Assertions.assertTrue(Reviews.stream().anyMatch(f -> f.getId().equals(testReview.getId())));
  }

  public void assertReview(ReviewCreate request, Review testReview) {
    Assertions.assertNotNull(testReview);
  }

  @Test
  @Order(3)
  public void testReviewUpdate() {
    ReviewUpdate request = new ReviewUpdate().setId(testReview.getId());
    ResponseEntity<Review> response =
        this.restTemplate.exchange(
            "/Review/updateReview", HttpMethod.PUT, new HttpEntity<>(request), Review.class);
    Assertions.assertEquals(200, response.getStatusCodeValue());
    testReview = response.getBody();
    assertReview(request, testReview);
  }
}
