package com.example.demo.owner.store.management;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.owner.OwnerService;
import com.example.demo.owner.store.Store;
import com.example.demo.owner.store.StoreService;
import com.example.demo.owner.store.management.menu.StoreMenu;
import com.example.demo.owner.store.management.menu.StoreMenuDTO;
import com.example.demo.owner.store.management.menu.StoreMenuRepository;

@Service
public class ManagementService {

    private final StoreBasicRepository storeBasicRepository;
    private final StoreMenuRepository storeMenuRepository;
    private final OwnerService ownerService;
    private final StoreService storeService;

    public ManagementService(StoreBasicRepository storeBasicRepository, StoreMenuRepository storeMenuRepository,
                             OwnerService ownerService, StoreService storeService) {
        this.storeBasicRepository = storeBasicRepository;
        this.storeMenuRepository = storeMenuRepository;
        this.ownerService = ownerService;
        this.storeService = storeService;
    }

    // 가게 id로 가게 구성 찾기
    public StoreBasic findByStoreId(Long storeId) {
        StoreBasic basic = storeBasicRepository.findByStoreId(storeId).orElse(null);

        return basic;
    }

    public void saveStoreBaisc(String weekdayStartHour, String weekdayEndHour,
     String weekdayStartMinute, String weekdayEndMinute, String weekendStartHour,
     String weekendEndHour, String weekendStartMinute, String weekendEndMinute, StoreBasicDTO storeBasicDTO) {
        if(weekdayStartHour.length() < 2) weekdayStartHour = "0" + weekdayStartHour;
        if(weekdayEndHour.length() < 2) weekdayEndHour = "0" + weekdayEndHour;
        if(weekdayStartMinute.length() < 2) weekdayStartMinute = "0" + weekdayStartMinute;
        if(weekdayEndMinute.length() < 2) weekdayEndMinute = "0" + weekdayEndMinute;
        if(weekendStartHour.length() < 2) weekendStartHour = "0" + weekendStartHour;
        if(weekendEndHour.length() < 2) weekendEndHour = "0" + weekendEndHour;
        if(weekendStartMinute.length() < 2) weekendStartMinute = "0" + weekendStartMinute;
        if(weekendEndMinute.length() < 2) weekendEndMinute = "0" + weekendEndMinute;

        Long ownerId = ownerService.getLoggedInOwnerId();
        Store store = storeService.findByOwnerId(ownerId);
        StoreBasic basic = findByStoreId(store.getStoreId());

        basic.setStoreId(store.getStoreId());
        basic.setPhoneNumber(storeBasicDTO.getPhoneNumber());
        basic.setWeekdayStartTime(weekdayStartHour + ":" + weekdayStartMinute);
        basic.setWeekdayEndTime(weekdayEndHour + ":" + weekdayEndMinute);
        basic.setWeekendStartTime(weekendStartHour + ":" + weekendStartMinute);
        basic.setWeekendEndTime(weekendEndHour + ":" + weekendEndMinute);
        basic.setDelCost(storeBasicDTO.getDelCost());
        basic.setDelMinCost(storeBasicDTO.getDelMinCost());
        basic.setIntroduction(storeBasicDTO.getIntroduction());
        basic.setOwnerName(storeBasicDTO.getOwnerName());
        
        storeBasicRepository.save(basic);
    }
    // 등록된 메뉴 리스트
    public List<StoreMenu> findAllStoreMenu(Long storeId) {
        List<StoreMenu> menu = storeMenuRepository.findAllByStoreId(storeId);

        return menu;
    }

    // 메뉴 등록하기
    public void saveStoreMenu(StoreMenuDTO storeMenuDTO, MultipartFile image) {
        String uploadDir = "src/main/resources/static/images/store/food";
        Long ownerId = ownerService.getLoggedInOwnerId();
        Store store = storeService.findByOwnerId(ownerId);

        String newFileName = saveImage(image ,uploadDir);

        StoreMenu menu = new StoreMenu();
        menu.setStoreId(store.getStoreId());
        menu.setName(storeMenuDTO.getName());
        menu.setCost(storeMenuDTO.getCost());
        menu.setIntroduction(storeMenuDTO.getIntroduction());
        menu.setComposition(storeMenuDTO.getComposition());
        menu.setIsMain(storeMenuDTO.getIsMain());
        menu.setImageName(newFileName);
        menu.setImagePath(uploadDir + newFileName);
        storeMenuRepository.save(menu);
    }

    // 이미지 실제 경로 저장 및 파일 이름 변경
    private String saveImage(MultipartFile image, String uploadDir) {
        try {
            // 원래 파일 이름 및 확장자 추출
            String originalFileName = image.getOriginalFilename();
            String extension = originalFileName.substring(originalFileName.lastIndexOf("."));

            // UUID로 새로운 파일 이름 생성
            String uuid = UUID.randomUUID().toString();
            String newFileName = uuid + extension;

            // 파일 저장 경로 설정
            Path path = Paths.get(uploadDir + newFileName);
            Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            return newFileName;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }

    }

    
}
