package com.kent.gmail.com.runtime.service;

import com.kent.gmail.com.runtime.data.ProductRepository;
import com.kent.gmail.com.runtime.model.Product;
import com.kent.gmail.com.runtime.model.Product_;
import com.kent.gmail.com.runtime.request.ProductCreate;
import com.kent.gmail.com.runtime.request.ProductFilter;
import com.kent.gmail.com.runtime.request.ProductUpdate;
import com.kent.gmail.com.runtime.response.PaginationResponse;
import com.kent.gmail.com.runtime.security.UserSecurityContext;
import jakarta.persistence.metamodel.SingularAttribute;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class ProductService {

  @Autowired private ProductRepository repository;

  @Autowired private BaseService baseService;

  /**
   * @param productCreate Object Used to Create Product
   * @param securityContext
   * @return created Product
   */
  public Product createProduct(ProductCreate productCreate, UserSecurityContext securityContext) {
    Product product = createProductNoMerge(productCreate, securityContext);
    product = this.repository.merge(product);
    return product;
  }

  /**
   * @param productCreate Object Used to Create Product
   * @param securityContext
   * @return created Product unmerged
   */
  public Product createProductNoMerge(
      ProductCreate productCreate, UserSecurityContext securityContext) {
    Product product = new Product();
    product.setId(UUID.randomUUID().toString());
    updateProductNoMerge(product, productCreate);

    return product;
  }

  /**
   * @param productCreate Object Used to Create Product
   * @param product
   * @return if product was updated
   */
  public boolean updateProductNoMerge(Product product, ProductCreate productCreate) {
    boolean update = baseService.updateBaseNoMerge(product, productCreate);

    if (productCreate.getPrice() != null
        && (!productCreate.getPrice().equals(product.getPrice()))) {
      product.setPrice(productCreate.getPrice());
      update = true;
    }

    if (productCreate.getReview() != null
        && (product.getReview() == null
            || !productCreate.getReview().getId().equals(product.getReview().getId()))) {
      product.setReview(productCreate.getReview());
      update = true;
    }

    return update;
  }

  /**
   * @param productUpdate
   * @param securityContext
   * @return product
   */
  public Product updateProduct(ProductUpdate productUpdate, UserSecurityContext securityContext) {
    Product product = productUpdate.getProduct();
    if (updateProductNoMerge(product, productUpdate)) {
      product = this.repository.merge(product);
    }
    return product;
  }

  /**
   * @param productFilter Object Used to List Product
   * @param securityContext
   * @return PaginationResponse<Product> containing paging information for Product
   */
  public PaginationResponse<Product> getAllProducts(
      ProductFilter productFilter, UserSecurityContext securityContext) {
    List<Product> list = listAllProducts(productFilter, securityContext);
    long count = this.repository.countAllProducts(productFilter, securityContext);
    return new PaginationResponse<>(list, productFilter.getPageSize(), count);
  }

  /**
   * @param productFilter Object Used to List Product
   * @param securityContext
   * @return List of Product
   */
  public List<Product> listAllProducts(
      ProductFilter productFilter, UserSecurityContext securityContext) {
    return this.repository.listAllProducts(productFilter, securityContext);
  }

  public Product deleteProduct(String id, UserSecurityContext securityContext) {
    Product product =
        this.repository.getByIdOrNull(Product.class, Product_.id, id, securityContext);
    ;
    if (product == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product not found");
    }

    this.repository.remove(product);

    return product;
  }

  public <T extends Product, I> List<T> listByIds(
      Class<T> c,
      SingularAttribute<? super T, I> idField,
      Set<I> ids,
      UserSecurityContext securityContext) {
    return repository.listByIds(c, idField, ids, securityContext);
  }

  public <T extends Product, I> T getByIdOrNull(
      Class<T> c,
      SingularAttribute<? super T, I> idField,
      I id,
      UserSecurityContext securityContext) {
    return repository.getByIdOrNull(c, idField, id, securityContext);
  }

  public <T extends Product, I> T getByIdOrNull(
      Class<T> c, SingularAttribute<? super T, I> idField, I id) {
    return repository.getByIdOrNull(c, idField, id);
  }

  public <T extends Product, I> List<T> listByIds(
      Class<T> c, SingularAttribute<? super T, I> idField, Set<I> ids) {
    return repository.listByIds(c, idField, ids);
  }

  public <T> T merge(T base) {
    return this.repository.merge(base);
  }

  public void massMerge(List<?> toMerge) {
    this.repository.massMerge(toMerge);
  }
}
