package com.example.demo.owner.store.management;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.ImageService;
import com.example.demo.owner.OwnerService;
import com.example.demo.owner.store.Store;
import com.example.demo.owner.store.StoreService;
import com.example.demo.owner.store.management.menu.StoreMenu;
import com.example.demo.owner.store.management.menu.StoreMenuDTO;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Controller
@RequestMapping("/mgmt")
public class ManagementController {

    private final StoreService storeService;
    private final OwnerService ownerService;
    
    private final ManagementService managementService;
    private final ImageService imageService;
    public ManagementController(StoreService storeService, OwnerService ownerService, 
    ManagementService managementService, ImageService imageService) {
        this.storeService = storeService;
        this.ownerService = ownerService;
        this.managementService = managementService;
        this.imageService = imageService;
    }

    @ModelAttribute
    public String storeModel(Model model) {
        Long ownerId = ownerService.getLoggedInOwnerId();
        Store store = storeService.findByOwnerId(ownerId);
        System.out.println(store.getStoreId());
        model.addAttribute("store", store);
        StoreBasic basic = managementService.findByStoreId(store.getStoreId());
        
        if(basic == null){
            model.addAttribute("basic", new StoreBasic());
            model.addAttribute("weekdayStartTime", new String[]{"00","00"});
            model.addAttribute("weekdayEndTime", new String[]{"00:00","00"});
            model.addAttribute("weekendStartTime", new String[]{"00:00","00"});
            model.addAttribute("weekendEndTime", new String[]{"00:00","00"});
            return "redirect:/mgmt/store-basic";
        } 
        String[] weekdayStartTime = basic.getWeekdayStartTime().split(":");
        String[] weekdayEndTime = basic.getWeekdayEndTime().split(":");
        String[] weekendStartTime = basic.getWeekendStartTime().split(":");
        String[] weekendEndTime = basic.getWeekendEndTime().split(":");

        model.addAttribute("weekdayStartTime", weekdayStartTime);
        model.addAttribute("weekdayEndTime", weekdayEndTime);
        model.addAttribute("weekendStartTime", weekendStartTime);
        model.addAttribute("weekendEndTime", weekendEndTime);
        model.addAttribute("basic", basic);
        
        return "";

    }

    // 가게 기본 설정으로 이동
    @GetMapping("/store-basic/form")
    public String storeBasic() {
        return "owner/store/mgmt/store-basic";
    }

    // 가게 기본 설정 등록(수정)
    @PostMapping("/save-basic")
    public String saveBasic(@ModelAttribute StoreBasicDTO storeBasicDTO,
                            @RequestParam("weekdayStartHour") String weekdayStartHour,
                            @RequestParam("weekdayEndHour") String weekdayEndHour,
                            @RequestParam("weekdayStartMinute") String weekdayStartMinute,
                            @RequestParam("weekdayEndMinute") String weekdayEndMinute,
                            @RequestParam("weekendStartHour") String weekendStartHour,
                            @RequestParam("weekendEndHour") String weekendEndHour,
                            @RequestParam("weekendStartMinute") String weekendStartMinute,
                            @RequestParam("weekendEndMinute") String weekendEndMinute) {
        
        managementService.saveStoreBaisc(weekdayStartHour, weekdayEndHour, weekdayStartMinute, weekdayEndMinute,
                                         weekendStartHour, weekendEndHour, weekendStartMinute, weekendEndMinute, storeBasicDTO);
        
        return "redirect:/owner/menu";
    }
    
    // 메뉴 이동
    @GetMapping("/food-menu")
    public String foodMenu(Model model) {
        List<StoreMenu> menus = managementService.findAllStoreMenu();
        model.addAttribute("menus", menus);
        return "owner/store/mgmt/food-menu";
    }
    // 메뉴 등록 페이지
    @GetMapping("/form/food-register")
    public String formFoodRegister(Model model) {
        model.addAttribute("menu", new StoreMenu("/images/user/noImage.png"));
        return "owner/store/mgmt/food-register";
    }

    // 메뉴 수정 페이지
    @PostMapping("/form/food-register")
    public String formFoodUpdate(Model model, @RequestParam("storeMenuId") Long storeMenuId) {
        StoreMenu menu = managementService.findByMenuIdFromStoreMenu(storeMenuId);
        if(menu.getImagePath() == null) menu.setImagePath("/images/user/noImage.png");
        model.addAttribute("menu", menu);
        return "owner/store/mgmt/food-register";
    }
    
    // 메뉴 등록(수정)
    @PostMapping("/food-register")
    public String foodRegister(@ModelAttribute StoreMenuDTO storeMenuDTO,
                               @RequestParam(value = "image", required = false) MultipartFile image,
                               @RequestParam(value = "preImageName", required = false) String preImageName) {
        // 기존 이미지 파일 삭제
        if(!image.isEmpty())
        imageService.deleteImage("/images/store/food/" + preImageName);
        System.out.println("이름"+preImageName+"이름");

        managementService.saveStoreMenu(storeMenuDTO, image, preImageName);
        return "redirect:/mgmt/food-menu";
    }

    // 테이블 개수 관리 페이지 이동
    @GetMapping("/store-table/form")
    public String storeTableForm(Model model) {
        
        Long ownerId = ownerService.getLoggedInOwnerId();
        Store store = storeService.findByOwnerId(ownerId);
        StoreTable table = managementService.getTable(store.getStoreId());

        model.addAttribute("table", table);
        return "owner/store/mgmt/store-table";
    }

    // 테이블 정보 저장
    @PostMapping("/save-table")
    public String saveTable(@ModelAttribute StoreTableDTO dto) {
        Long ownerId = ownerService.getLoggedInOwnerId();
        Store store = storeService.findByOwnerId(ownerId);
        managementService.setStoreReservationTable(dto, store.getStoreId());
        managementService.saveStoreTable(dto, store.getStoreId());
        return "redirect:/owner/menu";
    }
    
    
    

}
