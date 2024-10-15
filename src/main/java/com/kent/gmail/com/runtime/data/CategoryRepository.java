package com.kent.gmail.com.runtime.data;

import com.kent.gmail.com.runtime.model.Category;
import com.kent.gmail.com.runtime.model.Category_;
import com.kent.gmail.com.runtime.model.Product;
import com.kent.gmail.com.runtime.model.Product_;
import com.kent.gmail.com.runtime.request.CategoryFilter;
import com.kent.gmail.com.runtime.security.UserSecurityContext;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import jakarta.persistence.metamodel.SingularAttribute;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class CategoryRepository {
  @PersistenceContext private EntityManager em;

  @Autowired private ApplicationEventPublisher applicationEventPublisher;

  @Autowired private BaseRepository baseRepository;

  /**
   * @param categoryFilter Object Used to List Category
   * @param securityContext
   * @return List of Category
   */
  public List<Category> listAllCategories(
      CategoryFilter categoryFilter, UserSecurityContext securityContext) {
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<Category> q = cb.createQuery(Category.class);
    Root<Category> r = q.from(Category.class);
    List<Predicate> preds = new ArrayList<>();
    addCategoryPredicate(categoryFilter, cb, q, r, preds, securityContext);
    q.select(r).where(preds.toArray(new Predicate[0]));
    TypedQuery<Category> query = em.createQuery(q);

    if (categoryFilter.getPageSize() != null
        && categoryFilter.getCurrentPage() != null
        && categoryFilter.getPageSize() > 0
        && categoryFilter.getCurrentPage() > -1) {
      query
          .setFirstResult(categoryFilter.getPageSize() * categoryFilter.getCurrentPage())
          .setMaxResults(categoryFilter.getPageSize());
    }

    return query.getResultList();
  }

  public <T extends Category> void addCategoryPredicate(
      CategoryFilter categoryFilter,
      CriteriaBuilder cb,
      CommonAbstractCriteria q,
      From<?, T> r,
      List<Predicate> preds,
      UserSecurityContext securityContext) {

    baseRepository.addBasePredicate(categoryFilter, cb, q, r, preds, securityContext);

    if (categoryFilter.getProducts() != null && !categoryFilter.getProducts().isEmpty()) {
      Set<String> ids =
          categoryFilter.getProducts().parallelStream()
              .map(f -> f.getId())
              .collect(Collectors.toSet());
      Join<T, Product> join = r.join(Category_.product);
      preds.add(join.get(Product_.id).in(ids));
    }
  }

  /**
   * @param categoryFilter Object Used to List Category
   * @param securityContext
   * @return count of Category
   */
  public Long countAllCategories(
      CategoryFilter categoryFilter, UserSecurityContext securityContext) {
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<Long> q = cb.createQuery(Long.class);
    Root<Category> r = q.from(Category.class);
    List<Predicate> preds = new ArrayList<>();
    addCategoryPredicate(categoryFilter, cb, q, r, preds, securityContext);
    q.select(cb.count(r)).where(preds.toArray(new Predicate[0]));
    TypedQuery<Long> query = em.createQuery(q);
    return query.getSingleResult();
  }

  public void remove(Object o) {
    em.remove(o);
  }

  public <T extends Category, I> List<T> listByIds(
      Class<T> c,
      SingularAttribute<? super T, I> idField,
      Set<I> ids,
      UserSecurityContext securityContext) {
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<T> q = cb.createQuery(c);
    Root<T> r = q.from(c);
    List<Predicate> preds = new ArrayList<>();
    preds.add(r.get(idField).in(ids));

    q.select(r).where(preds.toArray(new Predicate[0]));
    return em.createQuery(q).getResultList();
  }

  public <T extends Category, I> T getByIdOrNull(
      Class<T> c, SingularAttribute<? super T, I> idField, I id) {
    return getByIdOrNull(c, idField, id, null);
  }

  public <T extends Category, I> List<T> listByIds(
      Class<T> c, SingularAttribute<? super T, I> idField, Set<I> ids) {
    return listByIds(c, idField, ids, null);
  }

  public <T extends Category, I> T getByIdOrNull(
      Class<T> c,
      SingularAttribute<? super T, I> idField,
      I id,
      UserSecurityContext securityContext) {
    return listByIds(c, idField, Collections.singleton(id), securityContext).stream()
        .findFirst()
        .orElse(null);
  }

  @Transactional
  public <T> T merge(T base) {
    T updated = em.merge(base);
    applicationEventPublisher.publishEvent(updated);
    return updated;
  }

  @Transactional
  public void massMerge(List<?> toMerge) {
    for (Object o : toMerge) {
      java.lang.Object updated = em.merge(o);
      applicationEventPublisher.publishEvent(updated);
    }
  }
}
