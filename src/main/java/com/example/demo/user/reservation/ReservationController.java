package com.example.demo.user.reservation;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.address.AddressService;
import com.example.demo.address.UserAddress;
import com.example.demo.owner.OwnerService;
import com.example.demo.owner.store.StoreCombineDTO;
import com.example.demo.owner.store.management.ManagementService;

import java.util.List;
import java.util.Arrays;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
@RequestMapping("/reservation")
public class ReservationController {

    private final OwnerService ownerService;
    private final ReservationService reservationService;
    private final AddressService addressService;
    private final ManagementService managementService;

    public ReservationController(OwnerService ownerService, ReservationService reservationService, AddressService addressService,
    ManagementService managementService) {
        this.ownerService = ownerService;
        this.reservationService = reservationService;
        this.addressService = addressService;
        this.managementService = managementService;
    }

    // 예약 메인페이지
    @GetMapping("/main")
    public String mainReservation(Model model) {
        List<StoreCombineDTO> stores = reservationService.getStoreInformation();
        List<UserAddress> addresses = addressService.getUserAddress();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int addCnt = 0;
        for(UserAddress address : addresses) {
            addCnt++;
            if(address.getIsActive() == true) {
                model.addAttribute("address", address);
                break;
            } else if(addCnt == addresses.size()){
                address.setName("주소를 설정해주세요.");
                model.addAttribute("address", address);
            } 
        }
        if(addresses.isEmpty()){
            UserAddress address = new UserAddress();
            address.setName("주소를 설정해주세요");
            model.addAttribute("address", address);
        }  
        model.addAttribute("stores", stores);
        
        return "user/reservation/main";
    }

    // 카테고리별 정렬
    @GetMapping("/sort")
    public String sortByCategory(@RequestParam("category") String category, Model model) {
        if(category.equals("전체"))
        return "redirect:/reservation/main";
        List<UserAddress> addresses = addressService.getUserAddress();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int addCnt = 0;
        for(UserAddress address : addresses) {
            addCnt++;
            if(address.getIsActive() == true) {
                model.addAttribute("address", address);
                break;
            } else if(addCnt == addresses.size()){
                address.setName("주소를 설정해주세요.");
                model.addAttribute("address", address);
            } 
        }
        List<StoreCombineDTO> stores = reservationService.getStoreInformation(category);
        model.addAttribute("stores", stores);
        return "user/reservation/main";
    }
    
    @PostMapping("/store-detail/form")
    public String storeDetailForm(@RequestParam("storeId") String storeId) {
        
        
        return "user/reservation/store-detail";
    }
    

}
