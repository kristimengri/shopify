package com.kent.gmail.com.runtime.model;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.util.List;

@Entity
public class Order extends Base {

    @OneToMany(mappedBy = "order")
    private List<Review> reviews;
}
