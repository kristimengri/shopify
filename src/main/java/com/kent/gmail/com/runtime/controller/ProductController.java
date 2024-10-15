package com.kent.gmail.com.runtime.controller;

import com.kent.gmail.com.runtime.model.Product;
import com.kent.gmail.com.runtime.request.ProductCreate;
import com.kent.gmail.com.runtime.request.ProductFilter;
import com.kent.gmail.com.runtime.request.ProductUpdate;
import com.kent.gmail.com.runtime.response.PaginationResponse;
import com.kent.gmail.com.runtime.security.UserSecurityContext;
import com.kent.gmail.com.runtime.service.ProductService;
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
@RequestMapping("Product")
@Tag(name = "Product")
public class ProductController {

  @Autowired private ProductService productService;

  @PostMapping("createProduct")
  @Operation(summary = "createProduct", description = "Creates Product")
  public Product createProduct(
      @Validated(Create.class) @RequestBody ProductCreate productCreate,
      Authentication authentication) {

    UserSecurityContext securityContext = (UserSecurityContext) authentication.getPrincipal();

    return productService.createProduct(productCreate, securityContext);
  }

  @DeleteMapping("{id}")
  @Operation(summary = "deleteProduct", description = "Deletes Product")
  public Product deleteProduct(@PathVariable("id") String id, Authentication authentication) {

    UserSecurityContext securityContext = (UserSecurityContext) authentication.getPrincipal();

    return productService.deleteProduct(id, securityContext);
  }

  @PostMapping("getAllProducts")
  @Operation(summary = "getAllProducts", description = "lists Products")
  public PaginationResponse<Product> getAllProducts(
      @Valid @RequestBody ProductFilter productFilter, Authentication authentication) {

    UserSecurityContext securityContext = (UserSecurityContext) authentication.getPrincipal();

    return productService.getAllProducts(productFilter, securityContext);
  }

  @PutMapping("updateProduct")
  @Operation(summary = "updateProduct", description = "Updates Product")
  public Product updateProduct(
      @Validated(Update.class) @RequestBody ProductUpdate productUpdate,
      Authentication authentication) {

    UserSecurityContext securityContext = (UserSecurityContext) authentication.getPrincipal();

    return productService.updateProduct(productUpdate, securityContext);
  }
}
