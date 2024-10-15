package com.kent.gmail.com.runtime.data;

import com.kent.gmail.com.runtime.model.Base;
import com.kent.gmail.com.runtime.model.Base_;
import com.kent.gmail.com.runtime.request.BaseFilter;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class BaseRepository {
  @PersistenceContext private EntityManager em;

  @Autowired private ApplicationEventPublisher applicationEventPublisher;

  /**
   * @param baseFilter Object Used to List Base
   * @param securityContext
   * @return List of Base
   */
  public List<Base> listAllBases(BaseFilter baseFilter, UserSecurityContext securityContext) {
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<Base> q = cb.createQuery(Base.class);
    Root<Base> r = q.from(Base.class);
    List<Predicate> preds = new ArrayList<>();
    addBasePredicate(baseFilter, cb, q, r, preds, securityContext);
    q.select(r).where(preds.toArray(new Predicate[0]));
    TypedQuery<Base> query = em.createQuery(q);

    if (baseFilter.getPageSize() != null
        && baseFilter.getCurrentPage() != null
        && baseFilter.getPageSize() > 0
        && baseFilter.getCurrentPage() > -1) {
      query
          .setFirstResult(baseFilter.getPageSize() * baseFilter.getCurrentPage())
          .setMaxResults(baseFilter.getPageSize());
    }

    return query.getResultList();
  }

  public <T extends Base> void addBasePredicate(
      BaseFilter baseFilter,
      CriteriaBuilder cb,
      CommonAbstractCriteria q,
      From<?, T> r,
      List<Predicate> preds,
      UserSecurityContext securityContext) {

    if (baseFilter.getDescription() != null && !baseFilter.getDescription().isEmpty()) {
      preds.add(r.get(Base_.description).in(baseFilter.getDescription()));
    }

    if (baseFilter.getName() != null && !baseFilter.getName().isEmpty()) {
      preds.add(r.get(Base_.name).in(baseFilter.getName()));
    }

    if (baseFilter.getSoftDeleted() != null && !baseFilter.getSoftDeleted().isEmpty()) {
      preds.add(r.get(Base_.softDeleted).in(baseFilter.getSoftDeleted()));
    }

    if (baseFilter.getDateUpdatedStart() != null) {
      preds.add(
          cb.greaterThanOrEqualTo(r.get(Base_.dateUpdated), baseFilter.getDateUpdatedStart()));
    }
    if (baseFilter.getDateUpdatedEnd() != null) {
      preds.add(cb.lessThanOrEqualTo(r.get(Base_.dateUpdated), baseFilter.getDateUpdatedEnd()));
    }

    if (baseFilter.getId() != null && !baseFilter.getId().isEmpty()) {
      preds.add(r.get(Base_.id).in(baseFilter.getId()));
    }

    if (baseFilter.getDateCreatedStart() != null) {
      preds.add(
          cb.greaterThanOrEqualTo(r.get(Base_.dateCreated), baseFilter.getDateCreatedStart()));
    }
    if (baseFilter.getDateCreatedEnd() != null) {
      preds.add(cb.lessThanOrEqualTo(r.get(Base_.dateCreated), baseFilter.getDateCreatedEnd()));
    }
  }

  /**
   * @param baseFilter Object Used to List Base
   * @param securityContext
   * @return count of Base
   */
  public Long countAllBases(BaseFilter baseFilter, UserSecurityContext securityContext) {
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<Long> q = cb.createQuery(Long.class);
    Root<Base> r = q.from(Base.class);
    List<Predicate> preds = new ArrayList<>();
    addBasePredicate(baseFilter, cb, q, r, preds, securityContext);
    q.select(cb.count(r)).where(preds.toArray(new Predicate[0]));
    TypedQuery<Long> query = em.createQuery(q);
    return query.getSingleResult();
  }

  public void remove(Object o) {
    em.remove(o);
  }

  public <T extends Base, I> List<T> listByIds(
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

  public <T extends Base, I> T getByIdOrNull(
      Class<T> c, SingularAttribute<? super T, I> idField, I id) {
    return getByIdOrNull(c, idField, id, null);
  }

  public <T extends Base, I> List<T> listByIds(
      Class<T> c, SingularAttribute<? super T, I> idField, Set<I> ids) {
    return listByIds(c, idField, ids, null);
  }

  public <T extends Base, I> T getByIdOrNull(
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
