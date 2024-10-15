package com.kent.gmail.com.runtime.controller;

import com.kent.gmail.com.runtime.AppInit;
import com.kent.gmail.com.runtime.model.Base;
import com.kent.gmail.com.runtime.request.BaseCreate;
import com.kent.gmail.com.runtime.request.BaseFilter;
import com.kent.gmail.com.runtime.request.BaseUpdate;
import com.kent.gmail.com.runtime.request.LoginRequest;
import com.kent.gmail.com.runtime.response.PaginationResponse;
import java.time.OffsetDateTime;
import java.time.ZoneId;
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
public class BaseControllerTest {

  private Base testBase;
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
  public void testBaseCreate() {
    BaseCreate request = new BaseCreate();

    request.setDescription("test-string");

    request.setName("test-string");

    request.setSoftDeleted(true);

    request.setDateUpdated(OffsetDateTime.now());

    request.setDateCreated(OffsetDateTime.now());

    ResponseEntity<Base> response =
        this.restTemplate.postForEntity("/Base/createBase", request, Base.class);
    Assertions.assertEquals(200, response.getStatusCodeValue());
    testBase = response.getBody();
    assertBase(request, testBase);
  }

  @Test
  @Order(2)
  public void testListAllBases() {
    BaseFilter request = new BaseFilter();
    ParameterizedTypeReference<PaginationResponse<Base>> t = new ParameterizedTypeReference<>() {};

    ResponseEntity<PaginationResponse<Base>> response =
        this.restTemplate.exchange(
            "/Base/getAllBases", HttpMethod.POST, new HttpEntity<>(request), t);
    Assertions.assertEquals(200, response.getStatusCodeValue());
    PaginationResponse<Base> body = response.getBody();
    Assertions.assertNotNull(body);
    List<Base> Bases = body.getList();
    Assertions.assertNotEquals(0, Bases.size());
    Assertions.assertTrue(Bases.stream().anyMatch(f -> f.getId().equals(testBase.getId())));
  }

  public void assertBase(BaseCreate request, Base testBase) {
    Assertions.assertNotNull(testBase);

    if (request.getDescription() != null) {
      Assertions.assertEquals(request.getDescription(), testBase.getDescription());
    }

    if (request.getName() != null) {
      Assertions.assertEquals(request.getName(), testBase.getName());
    }

    if (request.getSoftDeleted() != null) {
      Assertions.assertEquals(request.getSoftDeleted(), testBase.getSoftDeleted());
    }

    if (request.getDateUpdated() != null) {
      Assertions.assertEquals(
          request.getDateUpdated().atZoneSameInstant(ZoneId.systemDefault()),
          testBase.getDateUpdated().atZoneSameInstant(ZoneId.systemDefault()));
    }

    if (request.getDateCreated() != null) {
      Assertions.assertEquals(
          request.getDateCreated().atZoneSameInstant(ZoneId.systemDefault()),
          testBase.getDateCreated().atZoneSameInstant(ZoneId.systemDefault()));
    }
  }

  @Test
  @Order(3)
  public void testBaseUpdate() {
    BaseUpdate request = new BaseUpdate().setId(testBase.getId());
    ResponseEntity<Base> response =
        this.restTemplate.exchange(
            "/Base/updateBase", HttpMethod.PUT, new HttpEntity<>(request), Base.class);
    Assertions.assertEquals(200, response.getStatusCodeValue());
    testBase = response.getBody();
    assertBase(request, testBase);
  }
}
