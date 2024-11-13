package com.example.demo.user.join;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.address.AddressService;
import com.example.demo.address.UserAddress;
import com.example.demo.owner.store.StoreCombineDTO;
import com.example.demo.reservation.ReservationService;


@Controller
@RequestMapping("/join")
public class JoinController {

    private final ReservationService reservationService;
    private final AddressService addressService;
    
    public JoinController(ReservationService reservationService, AddressService addressService){
        this.reservationService = reservationService;

        this.addressService = addressService;
    }
    @GetMapping("/main")
    public String main(Model model) {
        List<StoreCombineDTO> stores = reservationService.getStoreInformation();
        List<UserAddress> addresses = addressService.getUserAddress();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int addCnt = 0;
        for (UserAddress address : addresses) {
            addCnt++;
            if (address.getIsActive() == true) {
                model.addAttribute("address", address);
                break;
            } else if (addCnt == addresses.size()) {
                address.setName("주소를 설정해주세요.");
                model.addAttribute("address", address);
            }
        }
        if (addresses.isEmpty()) {
            UserAddress address = new UserAddress();
            address.setName("주소를 설정해주세요");
            model.addAttribute("address", address);
        }
        model.addAttribute("stores", stores);

        return "user/join/main";
    }
    

}
