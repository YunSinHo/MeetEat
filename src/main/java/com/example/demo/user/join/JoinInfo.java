package com.example.demo.user.join;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "join_info")
@Getter
@Setter
public class JoinInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "join_id")
    private Long joinId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "store_id", nullable = false)
    private Long storeId;
    @Column(name = "user_image_path", nullable = false)
    private String userImagePath;
    @Column(name = "store_image_path", nullable = false)
    private String storeImagePath;
    @Column(name = "user_count", nullable = false)
    private String userCount;
    @Column(name = "store_name", nullable = false)
    private String storeName;
    
    @Column(name = "required_count", nullable = false)
    private String requiredCount;
    @Column(name = "title")
    private String title;
    @Column(name = "content")
    private String content;

    @Column(name = "category")
    private String category;


    @Column(name = "create_date", nullable = false)
    private LocalDateTime createDate = LocalDateTime.now();

    @Column(name = "is_active", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 1")
    private Boolean isActive = true;
    // 기본 생성자
    public JoinInfo() {
    }

}
