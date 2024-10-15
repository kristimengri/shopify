package com.kent.gmail.com.runtime.controller;

import com.kent.gmail.com.runtime.model.Category;
import com.kent.gmail.com.runtime.request.CategoryCreate;
import com.kent.gmail.com.runtime.request.CategoryFilter;
import com.kent.gmail.com.runtime.request.CategoryUpdate;
import com.kent.gmail.com.runtime.response.PaginationResponse;
import com.kent.gmail.com.runtime.security.UserSecurityContext;
import com.kent.gmail.com.runtime.service.CategoryService;
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
@RequestMapping("Category")
@Tag(name = "Category")
public class CategoryController {

  @Autowired private CategoryService categoryService;

  @PostMapping("createCategory")
  @Operation(summary = "createCategory", description = "Creates Category")
  public Category createCategory(
      @Validated(Create.class) @RequestBody CategoryCreate categoryCreate,
      Authentication authentication) {

    UserSecurityContext securityContext = (UserSecurityContext) authentication.getPrincipal();

    return categoryService.createCategory(categoryCreate, securityContext);
  }

  @DeleteMapping("{id}")
  @Operation(summary = "deleteCategory", description = "Deletes Category")
  public Category deleteCategory(@PathVariable("id") String id, Authentication authentication) {

    UserSecurityContext securityContext = (UserSecurityContext) authentication.getPrincipal();

    return categoryService.deleteCategory(id, securityContext);
  }

  @PostMapping("getAllCategories")
  @Operation(summary = "getAllCategories", description = "lists Categories")
  public PaginationResponse<Category> getAllCategories(
      @Valid @RequestBody CategoryFilter categoryFilter, Authentication authentication) {

    UserSecurityContext securityContext = (UserSecurityContext) authentication.getPrincipal();

    return categoryService.getAllCategories(categoryFilter, securityContext);
  }

  @PutMapping("updateCategory")
  @Operation(summary = "updateCategory", description = "Updates Category")
  public Category updateCategory(
      @Validated(Update.class) @RequestBody CategoryUpdate categoryUpdate,
      Authentication authentication) {

    UserSecurityContext securityContext = (UserSecurityContext) authentication.getPrincipal();

    return categoryService.updateCategory(categoryUpdate, securityContext);
  }
}
