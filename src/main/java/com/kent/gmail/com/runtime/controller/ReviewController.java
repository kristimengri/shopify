package com.kent.gmail.com.runtime.controller;

import com.kent.gmail.com.runtime.model.Review;
import com.kent.gmail.com.runtime.request.ReviewCreate;
import com.kent.gmail.com.runtime.request.ReviewFilter;
import com.kent.gmail.com.runtime.request.ReviewUpdate;
import com.kent.gmail.com.runtime.response.PaginationResponse;
import com.kent.gmail.com.runtime.security.UserSecurityContext;
import com.kent.gmail.com.runtime.service.ReviewService;
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
@RequestMapping("Review")
@Tag(name = "Review")
public class ReviewController {

  @Autowired private ReviewService reviewService;

  @PostMapping("createReview")
  @Operation(summary = "createReview", description = "Creates Review")
  public Review createReview(
      @Validated(Create.class) @RequestBody ReviewCreate reviewCreate,
      Authentication authentication) {

    UserSecurityContext securityContext = (UserSecurityContext) authentication.getPrincipal();

    return reviewService.createReview(reviewCreate, securityContext);
  }

  @DeleteMapping("{id}")
  @Operation(summary = "deleteReview", description = "Deletes Review")
  public Review deleteReview(@PathVariable("id") String id, Authentication authentication) {

    UserSecurityContext securityContext = (UserSecurityContext) authentication.getPrincipal();

    return reviewService.deleteReview(id, securityContext);
  }

  @PostMapping("getAllReviews")
  @Operation(summary = "getAllReviews", description = "lists Reviews")
  public PaginationResponse<Review> getAllReviews(
      @Valid @RequestBody ReviewFilter reviewFilter, Authentication authentication) {

    UserSecurityContext securityContext = (UserSecurityContext) authentication.getPrincipal();

    return reviewService.getAllReviews(reviewFilter, securityContext);
  }

  @PutMapping("updateReview")
  @Operation(summary = "updateReview", description = "Updates Review")
  public Review updateReview(
      @Validated(Update.class) @RequestBody ReviewUpdate reviewUpdate,
      Authentication authentication) {

    UserSecurityContext securityContext = (UserSecurityContext) authentication.getPrincipal();

    return reviewService.updateReview(reviewUpdate, securityContext);
  }
}
