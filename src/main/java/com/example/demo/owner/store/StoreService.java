package com.example.demo.owner.store;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.owner.store.image.StoreImage;
import com.example.demo.owner.store.image.StoreImageRepository;

@Service
public class StoreService {

    private final StoreRepository storeRepository;
    private final StoreImageRepository storeImageRepository;
    public StoreService(StoreRepository storeRepository, StoreImageRepository storeImageRepository){
        this.storeRepository = storeRepository;
        this.storeImageRepository = storeImageRepository;
    }

    // id로 가게 찾기
    public Store findByOwnerId(Long ownerId) {
        Store store = storeRepository.findByOwnerId(ownerId)
                                   .orElse(null);

        return store;
    }

    // 가게 정보가 있는지
    public boolean isExistProfile(Long ownerId) {
        boolean isExist = storeRepository.existsByOwnerId(ownerId);
        return isExist;
    }
    // 가게 저장
    public void saveStore(Store store) {
        storeRepository.save(store);
    }

    // 가게 등록된 사진 찾기(대문)
    public List<StoreImage> findByStoreIdFromImage(Long storeId) {
        List<StoreImage> images = storeImageRepository.findByStoreId(storeId);

        return images;
    }

    // 기존 등록된 사진 삭제
    public void deleteByImageName(String imageName) {
        storeImageRepository.deleteByImageName(imageName);
    }
    public void saveStoreImage(Long storeId, String imageName, String imagePath, boolean isMain) {
        StoreImage images = new StoreImage(storeId, imageName, imagePath, isMain);
        storeImageRepository.save(images);
    }

    // 스토어 아이디로 스토어 찾기
    public Store findById(Long storeId) {
        Store store = storeRepository.findById(storeId).get();
        return store;
    }

    // 가게 전부 가져오기
    public List<Store> findAll() {
        List<Store> stores = storeRepository.findAll();
        
        return stores;
    }

    

}
