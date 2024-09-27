package com.example.demo.user.profile;

import com.example.demo.user.Users;

import jakarta.persistence.*;

@Entity
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

    public UserProfileImage(){}

}
