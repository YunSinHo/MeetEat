package com.example.demo.owner.profile;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreImageRepository extends JpaRepository<StoreImage, Long>{
    List<StoreImage> findByOwnerId(Long ownerId);

    void deleteByImageName(String imageName);
}
