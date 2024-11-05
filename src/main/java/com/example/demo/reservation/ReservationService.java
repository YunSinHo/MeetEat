package com.example.demo.reservation;

import java.util.List;
import java.util.Map;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.stereotype.Service;

import com.example.demo.owner.OwnerService;
import com.example.demo.owner.store.Store;
import com.example.demo.owner.store.StoreCombineDTO;
import com.example.demo.owner.store.StoreRepository;
import com.example.demo.owner.store.StoreService;
import com.example.demo.owner.store.image.StoreImage;
import com.example.demo.owner.store.management.ManagementService;
import com.example.demo.owner.store.management.StoreBasic;

@Service
public class ReservationService {
    private final OwnerService ownerService;
    private final StoreService storeSerivce;
    private final ManagementService managementService;
    

    public ReservationService(OwnerService ownerService, StoreService storeSerivce,
            ManagementService managementService) {
        this.ownerService = ownerService;
        this.storeSerivce = storeSerivce;
        this.managementService = managementService;
    }

    // 가게 오픈 시간 확인
    public Map<String, Object> checkOpen(Long storeId) {
        Map<String, Object> timeMap = new HashMap<>();
        String startTimeStr = "00:00"; // 시작 시간
        String endTimeStr = "00:00"; // 마감 시간

        boolean isWeekday = checkWeekday(); // 주말인지 평일인지

        StoreBasic basic = managementService.findByStoreId(storeId);
        if (basic == null) {
            timeMap.put("startTime", startTimeStr);
            timeMap.put("endTime", endTimeStr);
            timeMap.put("isOpen", false);

            return timeMap;
        } else if (isWeekday) {
            startTimeStr = basic.getWeekdayStartTime();
            endTimeStr = basic.getWeekdayEndTime();

        } else {
            startTimeStr = basic.getWeekendStartTime();
            endTimeStr = basic.getWeekendEndTime();
        }
        LocalDateTime now = LocalDateTime.now();
        LocalTime startTime = LocalTime.parse(startTimeStr);
        LocalTime endTime = LocalTime.parse(endTimeStr);

        // 현재 시간이 시작 시간과 마감 시간 사이인지 확인
        if (now.toLocalTime().isBefore(startTime) || now.toLocalTime().isAfter(endTime)) {
            timeMap.put("isOpen", false);
        } else {
            timeMap.put("isOpen", true);
        }
        timeMap.put("startTime", startTimeStr);
        timeMap.put("endTime", endTimeStr);

        return timeMap;
    }

    // 주말 평일 체크
    public boolean checkWeekday() {
        boolean isWeekday;
        LocalDate today = LocalDate.now();

        // 오늘의 요일 확인
        DayOfWeek dayOfWeek = today.getDayOfWeek();

        // 주말인지 확인
        if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
            isWeekday = false;
        } else {
            isWeekday = true;
        }
        return isWeekday;
    }

    // 가게의 통합 정보 가져오기(예약 메인페이지 구성)
    public List<StoreCombineDTO> getStoreInformation() {
        List<Long> ownerIds = ownerService.findAllOwnerId();
        List<Long> storeIds = new ArrayList<>();
        List<StoreCombineDTO> list = new ArrayList<>();
        for (Long ownerId : ownerIds) {
            Store store = storeSerivce.findByOwnerId(ownerId);
            if (store == null)
                continue;
            storeIds.add(store.getStoreId());
        }
        for (Long storeId : storeIds) {
            Store store = storeSerivce.findById(storeId);
            StoreCombineDTO dto = new StoreCombineDTO();
            StoreBasic basic = managementService.findByStoreId(storeId);
            if(basic == null) basic = new StoreBasic();
            Map<String, Object> timeMap = checkOpen(storeId);

            dto.setStartTime((String)timeMap.get("startTime"));
            dto.setEndTime((String)timeMap.get("endTime"));
            dto.setIsOpen((Boolean)timeMap.get("isOpen"));
            
            dto.setIntroduction(basic.getIntroduction());
            dto.setPhoneNumber(basic.getPhoneNumber());
            dto.setLat(store.getLat());
            dto.setLng(store.getLng());
            dto.setPhoneNumber(null);
            dto.setAddress(store.getAddress());
            dto.setStoreName(store.getStoreName());
            dto.setCategory(store.getCategory());
            dto.setHeart("4.5");
            dto.setRank("4.3");
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
    // 가게 클릭시 디테일 정보
    public StoreCombineDTO getStoreInformation(Long storeId) {

            Store store = storeSerivce.findById(storeId);
            StoreCombineDTO dto = new StoreCombineDTO();
            List<StoreImage> images = storeSerivce.findByStoreIdFromImage(storeId);
            if(images.isEmpty()) images = new ArrayList<>();
            StoreBasic basic = managementService.findByStoreId(storeId);
            if(basic == null) basic = new StoreBasic();
            Map<String, Object> timeMap = checkOpen(storeId);

            dto.setStartTime((String)timeMap.get("startTime"));
            dto.setEndTime((String)timeMap.get("endTime"));
            dto.setIsOpen((Boolean)timeMap.get("isOpen"));
            
            dto.setStoreImages(images);
            dto.setIntroduction(basic.getIntroduction());
            dto.setPhoneNumber(basic.getPhoneNumber());
            dto.setLat(store.getLat());
            dto.setLng(store.getLng());
            dto.setPhoneNumber(null);
            dto.setAddress(store.getAddress());
            dto.setStoreName(store.getStoreName());
            dto.setCategory(store.getCategory());
            dto.setHeart("4.5");
            dto.setRank("4.3");
            dto.setStoreId(storeId);
            
            List<StoreImage> storeImages = storeSerivce.findByStoreIdFromImage(storeId);
            for (StoreImage image : storeImages) {
                if (image.getIsMain() == true) {
                    dto.setStoreImagePath(image.getImagePath());
                    break;
                }
            }
        
        return dto;
    }

    // 카테고리에 따른 분류
    public List<StoreCombineDTO> getStoreInformation(String category) {
        List<Long> ownerIds = ownerService.findAllOwnerId();
        List<Long> storeIds = new ArrayList<>();
        List<StoreCombineDTO> list = new ArrayList<>();
        for (Long ownerId : ownerIds) {
            Store store = storeSerivce.findByOwnerId(ownerId);
            if (store == null)
                continue;
            storeIds.add(store.getStoreId());
        }
        for (Long storeId : storeIds) {
            Store store = storeSerivce.findById(storeId);
            StoreCombineDTO dto = new StoreCombineDTO();
            Map<String, Object> timeMap = checkOpen(storeId);

            dto.setStartTime((String)timeMap.get("startTime"));
            dto.setEndTime((String)timeMap.get("endTime"));
            dto.setIsOpen((Boolean)timeMap.get("isOpen"));
            dto.setAddress(store.getAddress());
            dto.setStoreName(store.getStoreName());
            dto.setCategory(store.getCategory());
            dto.setHeart("4.5");
            dto.setRank("4.3");
            dto.setStoreId(storeId);
            List<StoreImage> storeImages = storeSerivce.findByStoreIdFromImage(storeId);
            for (StoreImage image : storeImages) {
                if (image.getIsMain() == true) {
                    dto.setStoreImagePath(image.getImagePath());
                    break;
                }
            }
            if (category.equals(dto.getCategory()))
                list.add(dto);
        }
        return list;
    }

    // 예약 시간대 설정
    public List<String> getReservationTime(String startTime, String endTime) {
         DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime openTime = LocalTime.parse(startTime, formatter);
        LocalTime closeTime = LocalTime.parse(endTime, formatter);
        LocalTime currentTime = LocalTime.now();

        List<String> timeSlots = new ArrayList<>();
        LocalTime time = openTime;

        while (time.isBefore(closeTime.minusHours(0))) {
            if (time.isAfter(currentTime) || time.equals(currentTime)) {
                timeSlots.add(time.format(formatter));
            }
            time = time.plusHours(2); // 2시간 간격
        }

        return timeSlots;
    }


}
