package com.kent.gmail.com.runtime.request;

import jakarta.validation.constraints.Min;
import java.time.OffsetDateTime;
import java.util.Set;

/** Object Used to List Base */
public class BaseFilter {

  @Min(value = 0)
  private Integer currentPage;

  private OffsetDateTime dateCreatedEnd;

  private OffsetDateTime dateCreatedStart;

  private OffsetDateTime dateUpdatedEnd;

  private OffsetDateTime dateUpdatedStart;

  private Set<String> description;

  private Set<String> id;

  private Set<String> name;

  @Min(value = 1)
  private Integer pageSize;

  private Set<Boolean> softDeleted;

  private OffsetDateTime softDeletedEnd;

  /**
   * @return currentPage
   */
  public Integer getCurrentPage() {
    return this.currentPage;
  }

  /**
   * @param currentPage currentPage to set
   * @return BaseFilter
   */
  public <T extends BaseFilter> T setCurrentPage(Integer currentPage) {
    this.currentPage = currentPage;
    return (T) this;
  }

  /**
   * @return dateCreatedEnd
   */
  public OffsetDateTime getDateCreatedEnd() {
    return this.dateCreatedEnd;
  }

  /**
   * @param dateCreatedEnd dateCreatedEnd to set
   * @return BaseFilter
   */
  public <T extends BaseFilter> T setDateCreatedEnd(OffsetDateTime dateCreatedEnd) {
    this.dateCreatedEnd = dateCreatedEnd;
    return (T) this;
  }

  /**
   * @return dateCreatedStart
   */
  public OffsetDateTime getDateCreatedStart() {
    return this.dateCreatedStart;
  }

  /**
   * @param dateCreatedStart dateCreatedStart to set
   * @return BaseFilter
   */
  public <T extends BaseFilter> T setDateCreatedStart(OffsetDateTime dateCreatedStart) {
    this.dateCreatedStart = dateCreatedStart;
    return (T) this;
  }

  /**
   * @return dateUpdatedEnd
   */
  public OffsetDateTime getDateUpdatedEnd() {
    return this.dateUpdatedEnd;
  }

  /**
   * @param dateUpdatedEnd dateUpdatedEnd to set
   * @return BaseFilter
   */
  public <T extends BaseFilter> T setDateUpdatedEnd(OffsetDateTime dateUpdatedEnd) {
    this.dateUpdatedEnd = dateUpdatedEnd;
    return (T) this;
  }

  /**
   * @return dateUpdatedStart
   */
  public OffsetDateTime getDateUpdatedStart() {
    return this.dateUpdatedStart;
  }

  /**
   * @param dateUpdatedStart dateUpdatedStart to set
   * @return BaseFilter
   */
  public <T extends BaseFilter> T setDateUpdatedStart(OffsetDateTime dateUpdatedStart) {
    this.dateUpdatedStart = dateUpdatedStart;
    return (T) this;
  }

  /**
   * @return description
   */
  public Set<String> getDescription() {
    return this.description;
  }

  /**
   * @param description description to set
   * @return BaseFilter
   */
  public <T extends BaseFilter> T setDescription(Set<String> description) {
    this.description = description;
    return (T) this;
  }

  /**
   * @return id
   */
  public Set<String> getId() {
    return this.id;
  }

  /**
   * @param id id to set
   * @return BaseFilter
   */
  public <T extends BaseFilter> T setId(Set<String> id) {
    this.id = id;
    return (T) this;
  }

  /**
   * @return name
   */
  public Set<String> getName() {
    return this.name;
  }

  /**
   * @param name name to set
   * @return BaseFilter
   */
  public <T extends BaseFilter> T setName(Set<String> name) {
    this.name = name;
    return (T) this;
  }

  /**
   * @return pageSize
   */
  public Integer getPageSize() {
    return this.pageSize;
  }

  /**
   * @param pageSize pageSize to set
   * @return BaseFilter
   */
  public <T extends BaseFilter> T setPageSize(Integer pageSize) {
    this.pageSize = pageSize;
    return (T) this;
  }

  /**
   * @return softDeleted
   */
  public Set<Boolean> getSoftDeleted() {
    return this.softDeleted;
  }

  /**
   * @param softDeleted softDeleted to set
   * @return BaseFilter
   */
  public <T extends BaseFilter> T setSoftDeleted(Set<Boolean> softDeleted) {
    this.softDeleted = softDeleted;
    return (T) this;
  }

  /**
   * @return softDeletedEnd
   */
  public OffsetDateTime getSoftDeletedEnd() {
    return this.softDeletedEnd;
  }

  /**
   * @param softDeletedEnd softDeletedEnd to set
   * @return BaseFilter
   */
  public <T extends BaseFilter> T setSoftDeletedEnd(OffsetDateTime softDeletedEnd) {
    this.softDeletedEnd = softDeletedEnd;
    return (T) this;
  }
}
