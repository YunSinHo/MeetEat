package com.example.demo.user.reservation;

import java.util.List;
import java.util.ArrayList;

import org.springframework.stereotype.Service;

import com.example.demo.owner.OwnerService;
import com.example.demo.owner.store.Store;
import com.example.demo.owner.store.StoreCombineDTO;
import com.example.demo.owner.store.StoreRepository;
import com.example.demo.owner.store.StoreService;
import com.example.demo.owner.store.image.StoreImage;

@Service
public class ReservationService {
    private final OwnerService ownerService;
    private final StoreService storeSerivce;


    public ReservationService(OwnerService ownerService, StoreService storeSerivce) {
        this.ownerService = ownerService;
        this.storeSerivce = storeSerivce;
    }

    // 가게의 통합 정보 가져오기(예약 메인페이지 구성)
    public List<StoreCombineDTO> getStoreInformation() {
        List<Long> ownerIds = ownerService.findAllOwnerId();
        List<Long> storeIds = new ArrayList<>();
        List<StoreCombineDTO> list = new ArrayList<>();
        for (Long ownerId : ownerIds) {
            Store store = storeSerivce.findByOwnerId(ownerId);
            if(store == null) continue;
            storeIds.add(store.getStoreId());
        }
        for (Long storeId : storeIds) {
            Store store = storeSerivce.findById(storeId);
            StoreCombineDTO dto = new StoreCombineDTO();
            dto.setAddress(store.getAddress());
            dto.setCategory(store.getCategory());
            dto.setHeart("0");
            dto.setRank("0");
            dto.setStoreId(storeId);
            List<StoreImage> storeImages = storeSerivce.findByStoreIdFromImage(storeId);
            for (StoreImage image : storeImages) {
                if (image.getIsMain() == true) {
                    dto.setStoreImagePath(image.getImagePath());
                    break;
                }
            }
            list.add(dto);
        }
        return list;
    }

}
