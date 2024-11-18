package com.example.demo.user.join;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.address.AddressService;
import com.example.demo.address.UserAddress;
import com.example.demo.owner.store.Store;
import com.example.demo.owner.store.StoreCombineDTO;
import com.example.demo.owner.store.StoreService;
import com.example.demo.reservation.ReservationBasicDTO;
import com.example.demo.reservation.ReservationService;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestBody;



@Controller
@RequestMapping("/join")
public class JoinController {

    private final ReservationService reservationService;
    private final AddressService addressService;
    private final JoinService joinService;
    private final StoreService storeService;
    
    public JoinController(ReservationService reservationService, AddressService addressService, JoinService joinService,
    StoreService storeService){
        this.reservationService = reservationService;
        this.joinService = joinService;
        this.addressService = addressService;
        this.storeService = storeService;
    }
    @GetMapping("/main")
    public String main(Model model) {
        List<JoinInfo> infos = joinService.findAll();
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
        model.addAttribute("isJoin", true);
        model.addAttribute("join", infos);

        return "user/join/main";
    }
    
    @GetMapping("/store-select")
    public String storeSelect(@RequestParam(value="isJoin", required = false) Boolean isJoin) {
        
        return "redirect:/reservation/main?isJoin=" + isJoin;
    }
    @PostMapping("/join-confirm/form")
    public String joinForm(Model model, @RequestParam("storeId")String storeId) {
        Long id = Long.parseLong(storeId);
        String storeImage = storeService.getStoreMainImage(id);
        Store store = storeService.findById(id);

        
        model.addAttribute("store", store);
        model.addAttribute("storeImage", storeImage);
        return "user/join/join-confirm";
    }

    @PostMapping("/join-save")
    public String joinSave(@ModelAttribute JoinInfoDTO info) {
        joinService.saveJoin(info);
        
        return "redirect:/join/main";
    }
    
    

}
