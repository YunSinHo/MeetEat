package com.example.demo.owner.store.image;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreImageRepository extends JpaRepository<StoreImage, Long>{
    List<StoreImage> findByOwnerId(Long ownerId);

    void deleteByImageName(String imageName);
}
