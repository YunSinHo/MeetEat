package com.example.demo.owner.store.management;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.owner.OwnerService;
import com.example.demo.owner.store.Store;
import com.example.demo.owner.store.StoreService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


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
        System.out.println(ownerId);
        model.addAttribute("store", store);
        StoreBasic basic = managementService.findByStoreId(1L);
        System.out.println("스토어아이디" + store.getStoreId());
        
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
        for(int i = 0; i < 2; i++) {
            System.out.println(weekdayStartTime[0] + " " + weekdayStartTime[1]);
        }
        model.addAttribute("weekdayStartTime", weekdayStartTime);
        model.addAttribute("weekdayEndTime", weekdayEndTime);
        model.addAttribute("weekendStartTime", weekendStartTime);
        model.addAttribute("weekendEndTime", weekendEndTime);
        model.addAttribute("basic", basic);
        
        return "";

    }

    @GetMapping("/store-basic")
    public String storeBasic() {
        return "owner/store/mgmt/store-basic";
    }

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
        Long ownerId = ownerService.getLoggedInOwnerId();
        Store store = storeService.findByOwnerId(ownerId);
        StoreBasic basic = managementService.findByStoreId(store.getStoreId());
        if(weekdayStartHour.length() < 2) weekdayStartHour = "0" + weekdayStartHour;
        if(weekdayEndHour.length() < 2) weekdayEndHour = "0" + weekdayEndHour;
        if(weekdayStartMinute.length() < 2) weekdayStartMinute = "0" + weekdayStartMinute;
        if(weekdayEndMinute.length() < 2) weekdayEndMinute = "0" + weekdayEndMinute;
        if(weekendStartHour.length() < 2) weekendStartHour = "0" + weekendStartHour;
        if(weekendEndHour.length() < 2) weekendEndHour = "0" + weekendEndHour;
        if(weekendStartMinute.length() < 2) weekendStartMinute = "0" + weekendStartMinute;
        if(weekendEndMinute.length() < 2) weekendEndMinute = "0" + weekendEndMinute;
        basic.setPhoneNumber(storeBasicDTO.getPhoneNumber());
        basic.setWeekdayStartTime(weekdayStartHour + ":" + weekdayStartMinute);
        basic.setWeekdayEndTime(weekdayEndHour + ":" + weekdayEndMinute);
        basic.setWeekendStartTime(weekendStartHour + ":" + weekendStartMinute);
        basic.setWeekendEndTime(weekendEndHour + ":" + weekendEndMinute);
        basic.setDelCost(storeBasicDTO.getDelCost());
        basic.setDelMinCost(storeBasicDTO.getDelMinCost());
        basic.setIntroduction(storeBasicDTO.getIntroduction());
        basic.setOwnerName(storeBasicDTO.getOwnerName());
        managementService.saveStoreBaisc(basic);
        
        return "redirect:/owner/menu";
    }
    

}
