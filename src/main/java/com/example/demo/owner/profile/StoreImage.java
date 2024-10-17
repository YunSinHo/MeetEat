package com.example.demo.owner.profile;

import com.example.demo.user.Users;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "store_images")
public class StoreImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_image_id")
    private Long storeImageId;

    @Column(name = "owner_id", nullable = false)
    private Long ownerId;

    @Column(name = "image_path", nullable = false)
    private String imagePath;

    @Column(name = "image_name")
    private String imageName;

    @Column(name = "is_main", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean isMain = false;
    public StoreImage(){}

    public StoreImage(Long storeImageId, Long ownerId, String imagePath, String imageName, Boolean isMain){
        this.storeImageId = storeImageId;
        this.ownerId = ownerId;
        this.imagePath = imagePath;
        this.imageName = imageName;
        this.isMain = isMain;
    }
    
    public StoreImage(Long ownerId, String imagePath, String imageName, Boolean isMain){
        this.ownerId = ownerId;
        this.imagePath = imagePath;
        this.imageName = imageName;
        this.isMain = isMain;
    }

}
