package com.kent.gmail.com.runtime.service;

import com.kent.gmail.com.runtime.data.ReviewRepository;
import com.kent.gmail.com.runtime.model.Review;
import com.kent.gmail.com.runtime.model.Review_;
import com.kent.gmail.com.runtime.request.ReviewCreate;
import com.kent.gmail.com.runtime.request.ReviewFilter;
import com.kent.gmail.com.runtime.request.ReviewUpdate;
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
public class ReviewService {

  @Autowired private ReviewRepository repository;

  @Autowired private BaseService baseService;

  /**
   * @param reviewCreate Object Used to Create Review
   * @param securityContext
   * @return created Review
   */
  public Review createReview(ReviewCreate reviewCreate, UserSecurityContext securityContext) {
    Review review = createReviewNoMerge(reviewCreate, securityContext);
    review = this.repository.merge(review);
    return review;
  }

  /**
   * @param reviewCreate Object Used to Create Review
   * @param securityContext
   * @return created Review unmerged
   */
  public Review createReviewNoMerge(
      ReviewCreate reviewCreate, UserSecurityContext securityContext) {
    Review review = new Review();
    review.setId(UUID.randomUUID().toString());
    updateReviewNoMerge(review, reviewCreate);

    return review;
  }

  /**
   * @param reviewCreate Object Used to Create Review
   * @param review
   * @return if review was updated
   */
  public boolean updateReviewNoMerge(Review review, ReviewCreate reviewCreate) {
    boolean update = baseService.updateBaseNoMerge(review, reviewCreate);

    return update;
  }

  /**
   * @param reviewUpdate
   * @param securityContext
   * @return review
   */
  public Review updateReview(ReviewUpdate reviewUpdate, UserSecurityContext securityContext) {
    Review review = reviewUpdate.getReview();
    if (updateReviewNoMerge(review, reviewUpdate)) {
      review = this.repository.merge(review);
    }
    return review;
  }

  /**
   * @param reviewFilter Object Used to List Review
   * @param securityContext
   * @return PaginationResponse<Review> containing paging information for Review
   */
  public PaginationResponse<Review> getAllReviews(
      ReviewFilter reviewFilter, UserSecurityContext securityContext) {
    List<Review> list = listAllReviews(reviewFilter, securityContext);
    long count = this.repository.countAllReviews(reviewFilter, securityContext);
    return new PaginationResponse<>(list, reviewFilter.getPageSize(), count);
  }

  /**
   * @param reviewFilter Object Used to List Review
   * @param securityContext
   * @return List of Review
   */
  public List<Review> listAllReviews(
      ReviewFilter reviewFilter, UserSecurityContext securityContext) {
    return this.repository.listAllReviews(reviewFilter, securityContext);
  }

  public Review deleteReview(String id, UserSecurityContext securityContext) {
    Review review = this.repository.getByIdOrNull(Review.class, Review_.id, id, securityContext);
    ;
    if (review == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Review not found");
    }

    this.repository.remove(review);

    return review;
  }

  public <T extends Review, I> List<T> listByIds(
      Class<T> c,
      SingularAttribute<? super T, I> idField,
      Set<I> ids,
      UserSecurityContext securityContext) {
    return repository.listByIds(c, idField, ids, securityContext);
  }

  public <T extends Review, I> T getByIdOrNull(
      Class<T> c,
      SingularAttribute<? super T, I> idField,
      I id,
      UserSecurityContext securityContext) {
    return repository.getByIdOrNull(c, idField, id, securityContext);
  }

  public <T extends Review, I> T getByIdOrNull(
      Class<T> c, SingularAttribute<? super T, I> idField, I id) {
    return repository.getByIdOrNull(c, idField, id);
  }

  public <T extends Review, I> List<T> listByIds(
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
