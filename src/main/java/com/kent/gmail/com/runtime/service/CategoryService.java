package com.kent.gmail.com.runtime.service;

import com.kent.gmail.com.runtime.data.CategoryRepository;
import com.kent.gmail.com.runtime.model.Category;
import com.kent.gmail.com.runtime.model.Category_;
import com.kent.gmail.com.runtime.request.CategoryCreate;
import com.kent.gmail.com.runtime.request.CategoryFilter;
import com.kent.gmail.com.runtime.request.CategoryUpdate;
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
public class CategoryService {

  @Autowired private CategoryRepository repository;

  @Autowired private BaseService baseService;

  /**
   * @param categoryCreate Object Used to Create Category
   * @param securityContext
   * @return created Category
   */
  public Category createCategory(
      CategoryCreate categoryCreate, UserSecurityContext securityContext) {
    Category category = createCategoryNoMerge(categoryCreate, securityContext);
    category = this.repository.merge(category);
    return category;
  }

  /**
   * @param categoryCreate Object Used to Create Category
   * @param securityContext
   * @return created Category unmerged
   */
  public Category createCategoryNoMerge(
      CategoryCreate categoryCreate, UserSecurityContext securityContext) {
    Category category = new Category();
    category.setId(UUID.randomUUID().toString());
    updateCategoryNoMerge(category, categoryCreate);

    return category;
  }

  /**
   * @param categoryCreate Object Used to Create Category
   * @param category
   * @return if category was updated
   */
  public boolean updateCategoryNoMerge(Category category, CategoryCreate categoryCreate) {
    boolean update = baseService.updateBaseNoMerge(category, categoryCreate);

    if (categoryCreate.getProduct() != null
        && (category.getProduct() == null
            || !categoryCreate.getProduct().getId().equals(category.getProduct().getId()))) {
      category.setProduct(categoryCreate.getProduct());
      update = true;
    }

    return update;
  }

  /**
   * @param categoryUpdate
   * @param securityContext
   * @return category
   */
  public Category updateCategory(
      CategoryUpdate categoryUpdate, UserSecurityContext securityContext) {
    Category category = categoryUpdate.getCategory();
    if (updateCategoryNoMerge(category, categoryUpdate)) {
      category = this.repository.merge(category);
    }
    return category;
  }

  /**
   * @param categoryFilter Object Used to List Category
   * @param securityContext
   * @return PaginationResponse<Category> containing paging information for Category
   */
  public PaginationResponse<Category> getAllCategories(
      CategoryFilter categoryFilter, UserSecurityContext securityContext) {
    List<Category> list = listAllCategories(categoryFilter, securityContext);
    long count = this.repository.countAllCategories(categoryFilter, securityContext);
    return new PaginationResponse<>(list, categoryFilter.getPageSize(), count);
  }

  /**
   * @param categoryFilter Object Used to List Category
   * @param securityContext
   * @return List of Category
   */
  public List<Category> listAllCategories(
      CategoryFilter categoryFilter, UserSecurityContext securityContext) {
    return this.repository.listAllCategories(categoryFilter, securityContext);
  }

  public Category deleteCategory(String id, UserSecurityContext securityContext) {
    Category category =
        this.repository.getByIdOrNull(Category.class, Category_.id, id, securityContext);
    ;
    if (category == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category not found");
    }

    this.repository.remove(category);

    return category;
  }

  public <T extends Category, I> List<T> listByIds(
      Class<T> c,
      SingularAttribute<? super T, I> idField,
      Set<I> ids,
      UserSecurityContext securityContext) {
    return repository.listByIds(c, idField, ids, securityContext);
  }

  public <T extends Category, I> T getByIdOrNull(
      Class<T> c,
      SingularAttribute<? super T, I> idField,
      I id,
      UserSecurityContext securityContext) {
    return repository.getByIdOrNull(c, idField, id, securityContext);
  }

  public <T extends Category, I> T getByIdOrNull(
      Class<T> c, SingularAttribute<? super T, I> idField, I id) {
    return repository.getByIdOrNull(c, idField, id);
  }

  public <T extends Category, I> List<T> listByIds(
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
