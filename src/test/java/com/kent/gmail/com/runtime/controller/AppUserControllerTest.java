package com.kent.gmail.com.runtime.controller;

import com.kent.gmail.com.runtime.AppInit;
import com.kent.gmail.com.runtime.model.AppUser;
import com.kent.gmail.com.runtime.request.AppUserCreate;
import com.kent.gmail.com.runtime.request.AppUserFilter;
import com.kent.gmail.com.runtime.request.AppUserUpdate;
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
public class AppUserControllerTest {

  private AppUser testAppUser;
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
  public void testAppUserCreate() {
    AppUserCreate request = new AppUserCreate();

    request.setUsername("test-string");

    ResponseEntity<AppUser> response =
        this.restTemplate.postForEntity("/AppUser/createAppUser", request, AppUser.class);
    Assertions.assertEquals(200, response.getStatusCodeValue());
    testAppUser = response.getBody();
    assertAppUser(request, testAppUser);
  }

  @Test
  @Order(2)
  public void testListAllAppUsers() {
    AppUserFilter request = new AppUserFilter();
    ParameterizedTypeReference<PaginationResponse<AppUser>> t =
        new ParameterizedTypeReference<>() {};

    ResponseEntity<PaginationResponse<AppUser>> response =
        this.restTemplate.exchange(
            "/AppUser/getAllAppUsers", HttpMethod.POST, new HttpEntity<>(request), t);
    Assertions.assertEquals(200, response.getStatusCodeValue());
    PaginationResponse<AppUser> body = response.getBody();
    Assertions.assertNotNull(body);
    List<AppUser> AppUsers = body.getList();
    Assertions.assertNotEquals(0, AppUsers.size());
    Assertions.assertTrue(AppUsers.stream().anyMatch(f -> f.getId().equals(testAppUser.getId())));
  }

  public void assertAppUser(AppUserCreate request, AppUser testAppUser) {
    Assertions.assertNotNull(testAppUser);

    if (request.getUsername() != null) {
      Assertions.assertEquals(request.getUsername(), testAppUser.getUsername());
    }

    if (request.getPassword() != null) {
      Assertions.assertEquals(request.getPassword(), testAppUser.getPassword());
    }

    if (request.getRoles() != null) {
      Assertions.assertEquals(request.getRoles(), testAppUser.getRoles());
    }
  }

  @Test
  @Order(3)
  public void testAppUserUpdate() {
    AppUserUpdate request = new AppUserUpdate().setId(testAppUser.getId());
    ResponseEntity<AppUser> response =
        this.restTemplate.exchange(
            "/AppUser/updateAppUser", HttpMethod.PUT, new HttpEntity<>(request), AppUser.class);
    Assertions.assertEquals(200, response.getStatusCodeValue());
    testAppUser = response.getBody();
    assertAppUser(request, testAppUser);
  }
}
