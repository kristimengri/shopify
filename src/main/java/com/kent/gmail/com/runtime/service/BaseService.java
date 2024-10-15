package com.kent.gmail.com.runtime.service;

import com.kent.gmail.com.runtime.data.BaseRepository;
import com.kent.gmail.com.runtime.model.Base;
import com.kent.gmail.com.runtime.model.Base_;
import com.kent.gmail.com.runtime.request.BaseCreate;
import com.kent.gmail.com.runtime.request.BaseFilter;
import com.kent.gmail.com.runtime.request.BaseUpdate;
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
public class BaseService {

  @Autowired private BaseRepository repository;

  /**
   * @param baseCreate Object Used to Create Base
   * @param securityContext
   * @return created Base
   */
  public Base createBase(BaseCreate baseCreate, UserSecurityContext securityContext) {
    Base base = createBaseNoMerge(baseCreate, securityContext);
    base = this.repository.merge(base);
    return base;
  }

  /**
   * @param baseCreate Object Used to Create Base
   * @param securityContext
   * @return created Base unmerged
   */
  public Base createBaseNoMerge(BaseCreate baseCreate, UserSecurityContext securityContext) {
    Base base = new Base();
    base.setId(UUID.randomUUID().toString());
    updateBaseNoMerge(base, baseCreate);

    return base;
  }

  /**
   * @param baseCreate Object Used to Create Base
   * @param base
   * @return if base was updated
   */
  public boolean updateBaseNoMerge(Base base, BaseCreate baseCreate) {
    boolean update = false;

    if (baseCreate.getDescription() != null
        && (!baseCreate.getDescription().equals(base.getDescription()))) {
      base.setDescription(baseCreate.getDescription());
      update = true;
    }

    if (baseCreate.getName() != null && (!baseCreate.getName().equals(base.getName()))) {
      base.setName(baseCreate.getName());
      update = true;
    }

    if (baseCreate.getSoftDeleted() != null
        && (!baseCreate.getSoftDeleted().equals(base.getSoftDeleted()))) {
      base.setSoftDeleted(baseCreate.getSoftDeleted());
      update = true;
    }

    if (baseCreate.getDateUpdated() != null
        && (!baseCreate.getDateUpdated().equals(base.getDateUpdated()))) {
      base.setDateUpdated(baseCreate.getDateUpdated());
      update = true;
    }

    if (baseCreate.getDateCreated() != null
        && (!baseCreate.getDateCreated().equals(base.getDateCreated()))) {
      base.setDateCreated(baseCreate.getDateCreated());
      update = true;
    }

    return update;
  }

  /**
   * @param baseUpdate
   * @param securityContext
   * @return base
   */
  public Base updateBase(BaseUpdate baseUpdate, UserSecurityContext securityContext) {
    Base base = baseUpdate.getBase();
    if (updateBaseNoMerge(base, baseUpdate)) {
      base = this.repository.merge(base);
    }
    return base;
  }

  /**
   * @param baseFilter Object Used to List Base
   * @param securityContext
   * @return PaginationResponse<Base> containing paging information for Base
   */
  public PaginationResponse<Base> getAllBases(
      BaseFilter baseFilter, UserSecurityContext securityContext) {
    List<Base> list = listAllBases(baseFilter, securityContext);
    long count = this.repository.countAllBases(baseFilter, securityContext);
    return new PaginationResponse<>(list, baseFilter.getPageSize(), count);
  }

  /**
   * @param baseFilter Object Used to List Base
   * @param securityContext
   * @return List of Base
   */
  public List<Base> listAllBases(BaseFilter baseFilter, UserSecurityContext securityContext) {
    return this.repository.listAllBases(baseFilter, securityContext);
  }

  public Base deleteBase(String id, UserSecurityContext securityContext) {
    Base base = this.repository.getByIdOrNull(Base.class, Base_.id, id, securityContext);
    ;
    if (base == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Base not found");
    }

    this.repository.remove(base);

    return base;
  }

  public <T extends Base, I> List<T> listByIds(
      Class<T> c,
      SingularAttribute<? super T, I> idField,
      Set<I> ids,
      UserSecurityContext securityContext) {
    return repository.listByIds(c, idField, ids, securityContext);
  }

  public <T extends Base, I> T getByIdOrNull(
      Class<T> c,
      SingularAttribute<? super T, I> idField,
      I id,
      UserSecurityContext securityContext) {
    return repository.getByIdOrNull(c, idField, id, securityContext);
  }

  public <T extends Base, I> T getByIdOrNull(
      Class<T> c, SingularAttribute<? super T, I> idField, I id) {
    return repository.getByIdOrNull(c, idField, id);
  }

  public <T extends Base, I> List<T> listByIds(
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
