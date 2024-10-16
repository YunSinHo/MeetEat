package com.example.demo.user.profile.image;

import com.example.demo.user.Users;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "user_profile_images")
public class UserProfileImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_picture_id")
    private Long userPictureId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @Column(name = "image_path", nullable = false)
    private String imagePath;

    @Column(name = "image_name")
    private String imageName;

    @Column(name = "is_main", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean isMain = false;
    public UserProfileImage(){}

    public UserProfileImage(Long userPictureId, Users user, String imagePath, String imageName, Boolean isMain){
        this.userPictureId = userPictureId;
        this.user = user;
        this.imagePath = imagePath;
        this.imageName = imageName;
        this.isMain = isMain;
    }

}
