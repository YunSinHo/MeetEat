package com.example.demo.owner.store;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.owner.profile.StoreImage;
import com.example.demo.owner.profile.StoreImageRepository;

@Service
public class StoreService {

    private final StoreRepository storeRepository;
    private final StoreImageRepository storeImageRepository;
    public StoreService(StoreRepository storeRepository, StoreImageRepository storeImageRepository){
        this.storeRepository = storeRepository;
        this.storeImageRepository = storeImageRepository;
    }
    public boolean isExistProfile(Long ownerId) {
        boolean isExist = storeRepository.existsByOwnerId(ownerId);
        return isExist;
    }
    // 가게 저장
    public void saveStore(Store store) {
        storeRepository.save(store);
    }

    // 가게 등록된 사진 찾기(대문)
    public List<StoreImage> findByOwnerIdFromImage(Long ownerId) {
        List<StoreImage> images = storeImageRepository.findByOwnerId(ownerId);

        return images;
    }

    // 기존 등록된 사진 삭제
    public void deleteByImageName(String imageName) {
        storeImageRepository.deleteByImageName(imageName);

    }
    public void saveUserImage(Long ownerId, String imageName, String imagePath, boolean isMain) {
        StoreImage images = new StoreImage(ownerId, imageName, imagePath, isMain);
        storeImageRepository.save(images);
    }

}
