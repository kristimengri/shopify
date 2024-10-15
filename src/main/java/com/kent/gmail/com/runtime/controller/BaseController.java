package com.kent.gmail.com.runtime.controller;

import com.kent.gmail.com.runtime.model.Base;
import com.kent.gmail.com.runtime.request.BaseCreate;
import com.kent.gmail.com.runtime.request.BaseFilter;
import com.kent.gmail.com.runtime.request.BaseUpdate;
import com.kent.gmail.com.runtime.response.PaginationResponse;
import com.kent.gmail.com.runtime.security.UserSecurityContext;
import com.kent.gmail.com.runtime.service.BaseService;
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
@RequestMapping("Base")
@Tag(name = "Base")
public class BaseController {

  @Autowired private BaseService baseService;

  @PostMapping("createBase")
  @Operation(summary = "createBase", description = "Creates Base")
  public Base createBase(
      @Validated(Create.class) @RequestBody BaseCreate baseCreate, Authentication authentication) {

    UserSecurityContext securityContext = (UserSecurityContext) authentication.getPrincipal();

    return baseService.createBase(baseCreate, securityContext);
  }

  @DeleteMapping("{id}")
  @Operation(summary = "deleteBase", description = "Deletes Base")
  public Base deleteBase(@PathVariable("id") String id, Authentication authentication) {

    UserSecurityContext securityContext = (UserSecurityContext) authentication.getPrincipal();

    return baseService.deleteBase(id, securityContext);
  }

  @PostMapping("getAllBases")
  @Operation(summary = "getAllBases", description = "lists Bases")
  public PaginationResponse<Base> getAllBases(
      @Valid @RequestBody BaseFilter baseFilter, Authentication authentication) {

    UserSecurityContext securityContext = (UserSecurityContext) authentication.getPrincipal();

    return baseService.getAllBases(baseFilter, securityContext);
  }

  @PutMapping("updateBase")
  @Operation(summary = "updateBase", description = "Updates Base")
  public Base updateBase(
      @Validated(Update.class) @RequestBody BaseUpdate baseUpdate, Authentication authentication) {

    UserSecurityContext securityContext = (UserSecurityContext) authentication.getPrincipal();

    return baseService.updateBase(baseUpdate, securityContext);
  }
}
