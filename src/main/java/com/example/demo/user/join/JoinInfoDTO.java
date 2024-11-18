package com.example.demo.user.join;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JoinInfoDTO {
    private Long joinId;
    private Long userId;
    private Long storeId;
    private String userImagePath;
    private String storeImagePath;
    private String userCount;
    private String requiredCount;
    private String title;
    private String content;
    private LocalDateTime createDate;
    private Boolean isActive;
    private String category;
    private String storeName;
    public JoinInfoDTO() {
    }

}
