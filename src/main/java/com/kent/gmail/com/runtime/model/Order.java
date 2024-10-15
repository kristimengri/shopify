package com.kent.gmail.com.runtime.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.util.List;

@Entity
public class Order extends Base {

    @OneToMany(targetEntity = Review.class, mappedBy = "order")
    @JsonIgnore
    private List<Review> reviews;
}
