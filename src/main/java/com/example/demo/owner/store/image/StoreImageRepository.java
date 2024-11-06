package com.example.demo.owner.store.image;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreImageRepository extends JpaRepository<StoreImage, Long>{

    void deleteByImageName(String imageName);

    List<StoreImage> findByStoreId(Long storeId);
}
