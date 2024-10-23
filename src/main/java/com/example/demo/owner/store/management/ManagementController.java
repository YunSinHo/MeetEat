package com.example.demo.owner.store.management;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

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
    public ManagementController(StoreService storeService, OwnerService ownerService, ManagementService managementService) {
        this.storeService = storeService;
        this.ownerService = ownerService;
        this.managementService = managementService;
    }

    @ModelAttribute
    public String storeModel(Model model) {
        Long ownerId = ownerService.getLoggedInOwnerId();
        Store store = storeService.findByOwnerId(ownerId);
        System.out.println(store.getStoreId());
        model.addAttribute("store", store);
        StoreBasic basic = managementService.findByStoreId(store.getStoreId());
        
        if(basic.getStoreId() == null){
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
    @GetMapping("/store-basic")
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
        Long ownerId = ownerService.getLoggedInOwnerId();
        Store store = storeService.findByOwnerId(ownerId);
        List<StoreMenu> menus = managementService.findAllStoreMenu(store.getStoreId());
        model.addAttribute("menus", menus);
        return "owner/store/mgmt/food-menu";
    }
    // 메뉴 등록(수정) 페이지
    @GetMapping("/form/food-register")
    public String formFoodRegister() {
        return "owner/store/mgmt/food-register";
    }
    
    // 메뉴 등록(수정)
    @PostMapping("/food-register")
    public String foodRegister(@ModelAttribute StoreMenuDTO storeMenuDTO,
                               @RequestParam(value = "image", required = false) MultipartFile image) {
        managementService.saveStoreMenu(storeMenuDTO, image);
        return "redirect:/mgmt/food-menu";
    }
    
    

}
