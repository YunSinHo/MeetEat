package com.example.demo.owner.store.management;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.reservation.SRDRepository;
import com.example.demo.reservation.StoreReservationDate;

@Service
public class SRDService {
    
    private final SRDRepository srdRepository;

    public SRDService(SRDRepository srdRepository){
        this.srdRepository = srdRepository;
    }

    // 가게 테이블 개수 수정시 개수 업데이트
    public void updateStoreDates(List<StoreReservationDate> dates) {
        srdRepository.saveAll(dates);
    }
     // 가게 id로 예약 시간테이블 데이터 all
     public List<StoreReservationDate> findAllByStoreId(Long storeId) {
        List<StoreReservationDate> date = srdRepository.findAllByStoreId(storeId);

        return date;
    }
}
