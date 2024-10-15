package com.kent.gmail.com.runtime.data;

import com.kent.gmail.com.runtime.model.Category;
import com.kent.gmail.com.runtime.model.Category_;
import com.kent.gmail.com.runtime.model.Product;
import com.kent.gmail.com.runtime.model.Product_;
import com.kent.gmail.com.runtime.model.Review;
import com.kent.gmail.com.runtime.model.Review_;
import com.kent.gmail.com.runtime.request.ProductFilter;
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
public class ProductRepository {
  @PersistenceContext private EntityManager em;

  @Autowired private ApplicationEventPublisher applicationEventPublisher;

  @Autowired private BaseRepository baseRepository;

  /**
   * @param productFilter Object Used to List Product
   * @param securityContext
   * @return List of Product
   */
  public List<Product> listAllProducts(
      ProductFilter productFilter, UserSecurityContext securityContext) {
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<Product> q = cb.createQuery(Product.class);
    Root<Product> r = q.from(Product.class);
    List<Predicate> preds = new ArrayList<>();
    addProductPredicate(productFilter, cb, q, r, preds, securityContext);
    q.select(r).where(preds.toArray(new Predicate[0]));
    TypedQuery<Product> query = em.createQuery(q);

    if (productFilter.getPageSize() != null
        && productFilter.getCurrentPage() != null
        && productFilter.getPageSize() > 0
        && productFilter.getCurrentPage() > -1) {
      query
          .setFirstResult(productFilter.getPageSize() * productFilter.getCurrentPage())
          .setMaxResults(productFilter.getPageSize());
    }

    return query.getResultList();
  }

  public <T extends Product> void addProductPredicate(
      ProductFilter productFilter,
      CriteriaBuilder cb,
      CommonAbstractCriteria q,
      From<?, T> r,
      List<Predicate> preds,
      UserSecurityContext securityContext) {

    baseRepository.addBasePredicate(productFilter, cb, q, r, preds, securityContext);

    if (productFilter.getProductCategorieses() != null
        && !productFilter.getProductCategorieses().isEmpty()) {
      Set<String> ids =
          productFilter.getProductCategorieses().parallelStream()
              .map(f -> f.getId())
              .collect(Collectors.toSet());
      Join<T, Category> join = r.join(Product_.productCategories);
      preds.add(join.get(Category_.id).in(ids));
    }

    if (productFilter.getPriceStart() != null) {
      preds.add(cb.greaterThanOrEqualTo(r.get(Product_.price), productFilter.getPriceStart()));
    }
    if (productFilter.getPriceEnd() != null) {
      preds.add(cb.lessThanOrEqualTo(r.get(Product_.price), productFilter.getPriceEnd()));
    }

    if (productFilter.getReviews() != null && !productFilter.getReviews().isEmpty()) {
      Set<String> ids =
          productFilter.getReviews().parallelStream()
              .map(f -> f.getId())
              .collect(Collectors.toSet());
      Join<T, Review> join = r.join(Product_.review);
      preds.add(join.get(Review_.id).in(ids));
    }
  }

  /**
   * @param productFilter Object Used to List Product
   * @param securityContext
   * @return count of Product
   */
  public Long countAllProducts(ProductFilter productFilter, UserSecurityContext securityContext) {
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<Long> q = cb.createQuery(Long.class);
    Root<Product> r = q.from(Product.class);
    List<Predicate> preds = new ArrayList<>();
    addProductPredicate(productFilter, cb, q, r, preds, securityContext);
    q.select(cb.count(r)).where(preds.toArray(new Predicate[0]));
    TypedQuery<Long> query = em.createQuery(q);
    return query.getSingleResult();
  }

  public void remove(Object o) {
    em.remove(o);
  }

  public <T extends Product, I> List<T> listByIds(
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

  public <T extends Product, I> T getByIdOrNull(
      Class<T> c, SingularAttribute<? super T, I> idField, I id) {
    return getByIdOrNull(c, idField, id, null);
  }

  public <T extends Product, I> List<T> listByIds(
      Class<T> c, SingularAttribute<? super T, I> idField, Set<I> ids) {
    return listByIds(c, idField, ids, null);
  }

  public <T extends Product, I> T getByIdOrNull(
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
