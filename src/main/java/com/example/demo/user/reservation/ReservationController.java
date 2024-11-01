package com.example.demo.user.reservation;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.address.AddressService;
import com.example.demo.address.UserAddress;
import com.example.demo.owner.OwnerService;
import com.example.demo.owner.store.StoreCombineDTO;

import java.util.List;
import java.util.Arrays;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/reservation")
public class ReservationController {

    private final OwnerService ownerService;
    private final ReservationService reservationService;
    private final AddressService addressService;

    public ReservationController(OwnerService ownerService, ReservationService reservationService, AddressService addressService) {
        this.ownerService = ownerService;
        this.reservationService = reservationService;
        this.addressService = addressService;
    }

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
        System.out.println(category);
        List<StoreCombineDTO> stores = reservationService.getStoreInformation(category);
        model.addAttribute("stores", stores);
        return "user/reservation/main";
    }
    

}
