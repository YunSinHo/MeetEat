package com.example.demo.owner.store.image;

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

    @Column(name = "store_id", nullable = false)
    private Long storeId;

    @Column(name = "image_path", nullable = false)
    private String imagePath;

    @Column(name = "image_name")
    private String imageName;

    @Column(name = "is_main", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean isMain = false;
    public StoreImage(){}

    public StoreImage(Long storeImageId, Long storeId, String imagePath, String imageName, Boolean isMain){
        this.storeImageId = storeImageId;
        this.storeId = storeId;
        this.imagePath = imagePath;
        this.imageName = imageName;
        this.isMain = isMain;
    }
    
    public StoreImage(Long storeId, String imageName, String imagePath, Boolean isMain){
        this.storeId = storeId;
        this.imageName = imageName;
        this.imagePath = imagePath;
        this.isMain = isMain;
    }

}
