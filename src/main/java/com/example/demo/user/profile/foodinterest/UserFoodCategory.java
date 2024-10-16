package com.example.demo.user.profile.foodinterest;

import com.example.demo.owner.store.Categories;
import com.example.demo.user.Users;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "user_food_categories")
@Getter
@Setter
@ToString
public class UserFoodCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_category_id")
    private Long userCategoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Categories category;

    public UserFoodCategory(){}

}