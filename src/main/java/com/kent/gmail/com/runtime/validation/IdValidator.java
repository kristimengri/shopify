package com.kent.gmail.com.runtime.validation;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Id;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class IdValidator implements ConstraintValidator<IdValid, Object> {

  private static final Logger logger = LoggerFactory.getLogger(IdValidator.class);
  private String field;
  private Class<?> fieldType;
  private String targetField;
  @PersistenceContext @Lazy private EntityManager em;

  private final Map<String, PropertyDescriptor> idGettersCache = new ConcurrentHashMap<>();

  private final Map<String, PropertyDescriptor> creatorGettersCache = new ConcurrentHashMap<>();

  @Override
  public void initialize(IdValid constraintAnnotation) {
    this.field = constraintAnnotation.field();
    this.fieldType = constraintAnnotation.fieldType();
    this.targetField = constraintAnnotation.targetField();
  }

  @Override
  public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
    Authentication securityContext = SecurityContextHolder.getContext().getAuthentication();
    BeanWrapperImpl objectWrapper = new BeanWrapperImpl(value);

    Object fieldValue = objectWrapper.getPropertyValue(field);
    if (fieldValue instanceof Collection) {
      Collection<?> collection = (Collection<?>) fieldValue;
      Set<String> ids =
          collection.stream()
              .filter(f -> f instanceof String)
              .map(f -> (String) f)
              .collect(Collectors.toSet());
      if (!ids.isEmpty()) {
        Map<String, ?> basicMap = listByIds(fieldType, ids, securityContext);

        ids.removeAll(basicMap.keySet());
        if (!ids.isEmpty()) {
          constraintValidatorContext
              .buildConstraintViolationWithTemplate(
                  "cannot find " + fieldType.getCanonicalName() + " with ids \"" + ids + "\"")
              .addPropertyNode(field)
              .addConstraintViolation();
          return false;
        }
        objectWrapper.setPropertyValue(targetField, new ArrayList<>(basicMap.values()));
      }

    } else {
      if (fieldValue instanceof String) {
        String id = (String) fieldValue;

        Object basic = getByById(fieldType, id, securityContext);
        if (basic == null) {
          constraintValidatorContext
              .buildConstraintViolationWithTemplate(
                  "cannot find " + fieldType.getCanonicalName() + " with id \"" + id + "\"")
              .addPropertyNode(field)
              .addConstraintViolation();
          return false;
        }
        objectWrapper.setPropertyValue(targetField, basic);
      }
    }
    return true;
  }

  private PropertyDescriptor getIdPropertyDescriptor(Class<?> c) throws NoSuchMethodException {
    String canonicalName = c.getCanonicalName();
    PropertyDescriptor propertyDescriptor = idGettersCache.get(canonicalName);
    if (propertyDescriptor == null) {
      for (Field field : FieldUtils.getAllFields(c)) {
        if (field.isAnnotationPresent(Id.class)) {
          Method method = c.getMethod("get" + StringUtils.capitalize(field.getName()));
          propertyDescriptor = new PropertyDescriptor(method, field);
          break;
        }
      }
      if (propertyDescriptor != null) {
        idGettersCache.put(canonicalName, propertyDescriptor);
      }
    }
    return propertyDescriptor;
  }

  private <T> T getByById(Class<T> c, String id, Authentication securityContext) {
    return listByIds(c, Collections.singleton(id), securityContext).get(id);
  }

  private PropertyDescriptor getCreatorPropertyDescriptor(Class<?> c) throws NoSuchMethodException {
    String canonicalName = c.getCanonicalName();
    PropertyDescriptor propertyDescriptor = creatorGettersCache.get(canonicalName);
    if (propertyDescriptor == null) {
      for (Field field : FieldUtils.getAllFields(c)) {
        if (field.isAnnotationPresent(CreatedBy.class)) {
          Method method = c.getMethod("get" + StringUtils.capitalize(field.getName()));
          propertyDescriptor = new PropertyDescriptor(method, field);
          break;
        }
      }
      if (propertyDescriptor != null) {
        creatorGettersCache.put(canonicalName, propertyDescriptor);
      }
    }
    return propertyDescriptor;
  }

  private <T> Map<String, T> listByIds(
      Class<T> c, Set<String> ids, Authentication securityContext) {
    try {
      PropertyDescriptor idGetter = getIdPropertyDescriptor(c);
      PropertyDescriptor creatorGetter = getCreatorPropertyDescriptor(c);

      if (idGetter != null) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> q = cb.createQuery(c);
        Root<T> r = q.from(c);
        Predicate pred = r.get(idGetter.field.getName()).in(ids);
        com.kent.gmail.com.runtime.security.UserSecurityContext securityContextPrincipal =
            (com.kent.gmail.com.runtime.security.UserSecurityContext)
                securityContext.getPrincipal();
        if (creatorGetter != null && !securityContextPrincipal.isAdmin()) {
          pred =
              cb.and(
                  pred,
                  cb.equal(
                      r.get(creatorGetter.field.getName()), securityContextPrincipal.getUser()));
        }
        q.select(r).where(pred);
        return toMap(em.createQuery(q).getResultList(), idGetter.getter);
      }
    } catch (Exception e) {
      logger.error("failed getting " + c.getCanonicalName() + " by ids " + ids);
    }
    return Collections.emptyMap();
  }

  private <T> Map<String, T> toMap(List<T> resultList, Method idGetter)
      throws InvocationTargetException, IllegalAccessException {
    Map<String, T> map = new HashMap<>();
    for (T o : resultList) {

      map.put((String) idGetter.invoke(o), o);
    }
    return map;
  }

  private static class PropertyDescriptor {
    final Method getter;
    final Field field;

    public PropertyDescriptor(Method getter, Field field) {
      this.getter = getter;
      this.field = field;
    }
  }
}
