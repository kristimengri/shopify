package com.kent.gmail.com.runtime;

import com.kent.gmail.com.runtime.model.Product;
import com.kent.gmail.com.runtime.model.Review;
import com.kent.gmail.com.runtime.request.AppUserCreate;
import com.kent.gmail.com.runtime.request.ProductCreate;
import com.kent.gmail.com.runtime.request.ReviewCreate;
import com.kent.gmail.com.runtime.security.UserSecurityContext;
import com.kent.gmail.com.runtime.service.AppUserService;
import com.kent.gmail.com.runtime.service.ProductService;
import com.kent.gmail.com.runtime.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppInitConfig {

  @Autowired private ReviewService reviewService;

  @Autowired private ProductService productService;

  @Autowired
  @Qualifier("adminSecurityContext")
  private UserSecurityContext securityContext;

  @Bean
  public Review review() {
    ReviewCreate reviewCreate = new ReviewCreate();
    return reviewService.createReview(reviewCreate, securityContext);
  }

  @Bean
  public Product product() {
    ProductCreate productCreate = new ProductCreate();
    return productService.createProduct(productCreate, securityContext);
  }

  @Configuration
  public static class UserConfig {
    @Bean
    @Qualifier("adminSecurityContext")
    public UserSecurityContext adminSecurityContext(AppUserService appUserService) {
      com.kent.gmail.com.runtime.model.AppUser admin =
          appUserService.createAppUser(
              new AppUserCreate().setUsername("admin@flexicore.com").setPassword("admin"), null);
      return new UserSecurityContext(admin);
    }
  }
}
