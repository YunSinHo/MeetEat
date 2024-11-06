package com.example.demo.owner.store.management;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.ImageService;
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
    private final StoreTableRepository storeTableRepository;
    private final OwnerService ownerService;
    private final StoreService storeService;
    private final ImageService imageService;
    

    public ManagementService(StoreBasicRepository storeBasicRepository, StoreMenuRepository storeMenuRepository, 
    StoreTableRepository storeTableRepository, OwnerService ownerService, StoreService storeService, ImageService imageService) {
        this.storeBasicRepository = storeBasicRepository;
        this.storeMenuRepository = storeMenuRepository;
        this.ownerService = ownerService;
        this.storeService = storeService;
        this.imageService = imageService;
        this.storeTableRepository = storeTableRepository;
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
        if(basic == null) basic = new StoreBasic();
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
    public List<StoreMenu> findAllStoreMenu() {
        
        Long ownerId = ownerService.getLoggedInOwnerId();
        Store store = storeService.findByOwnerId(ownerId);
        List<StoreMenu> menu = storeMenuRepository.findAllByStoreId(store.getStoreId());

        return menu;
    }

    // 메뉴 등록하기
    public void saveStoreMenu(StoreMenuDTO storeMenuDTO, MultipartFile image, String preImageName) {
        String uploadDir = "/images/store/food/";
        Long ownerId = ownerService.getLoggedInOwnerId();
        Store store = storeService.findByOwnerId(ownerId);
        String newFileName;
        StoreMenu menu = new StoreMenu();
        
        menu.setStoreMenuId(storeMenuDTO.getStoreMenuId());
        menu.setStoreId(store.getStoreId());
        menu.setName(storeMenuDTO.getName());
        menu.setCost(storeMenuDTO.getCost());
        menu.setIntroduction(storeMenuDTO.getIntroduction());
        menu.setComposition(storeMenuDTO.getComposition());
        menu.setIsMain(storeMenuDTO.getIsMain());
        
        if(!image.isEmpty()){
            newFileName = saveImage(image ,uploadDir);
            menu.setImageName(newFileName);
            menu.setImagePath(uploadDir + newFileName);
        } else if(preImageName != null){
            menu.setImageName(preImageName);
            menu.setImagePath(uploadDir + preImageName);
        }
       
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
            Path path = Paths.get("src/main/resources/static" + uploadDir + newFileName);
            Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            return newFileName;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }

    }

    // 등록된 메뉴 id로 찾기
    public StoreMenu findByMenuIdFromStoreMenu(Long menuId) {
        StoreMenu menu = storeMenuRepository.findByStoreMenuId(menuId)
                                                      .orElseThrow(() -> new RuntimeException("menu not found with ID: " + menuId));
        
        return menu;
    }

    // 테이블 개수 세팅 찾기
    public StoreTable getTable(Long storeId) {
        StoreTable table = storeTableRepository.findById(storeId).orElse(new StoreTable());

        return table;
        
    }

    // 테이블 정보 저장
    public void saveStoreTable(StoreTableDTO dto, Long storeId) {
        StoreTable table = new StoreTable();
        
        table.setStoreId(storeId);
        table.setOneTable(dto.getOneTable());
        table.setTwoTable(dto.getTwoTable());
        table.setFourTable(dto.getFourTable());
        table.setPartyTable(dto.getPartyTable());
        storeTableRepository.save(table);
    }

    
}
