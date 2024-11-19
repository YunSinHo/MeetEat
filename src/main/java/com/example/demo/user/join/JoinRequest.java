package com.example.demo.user.join;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "join_request")
@Getter
@Setter
public class JoinRequest {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    private Long requestId;

    @Column(name = "join_id")
    private Long joinId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "other_id")
    private Long otherId;
    
    @Column(name = "is_accept", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 1")
    private Boolean isAccept = false;
    @Column(name = "is_refused", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 1")
    private Boolean isRefused = false;

    public JoinRequest(){}
}
