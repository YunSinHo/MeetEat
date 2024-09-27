package com.example.demo.user.interest;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "interests")
@Getter
@Setter
@ToString
public class Interest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "interest_id")
    private Long interestId;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    public Interest(){}
}