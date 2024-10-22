package com.example.demo.owner.store.management;

import org.springframework.stereotype.Service;

@Service
public class ManagementService {

    private final StoreBasicRepository storeBasicRepository;

    public ManagementService(StoreBasicRepository storeBasicRepository) {
        this.storeBasicRepository = storeBasicRepository;
    }

    // 가게 id로 가게 구성 찾기
    public StoreBasic findByStoreId(Long storeId) {
        StoreBasic basic = storeBasicRepository.findByStoreId(storeId).orElse(new StoreBasic());

        return basic;
    }

    public void saveStoreBaisc(StoreBasic basic) {
        storeBasicRepository.save(basic);
    }

    
}
