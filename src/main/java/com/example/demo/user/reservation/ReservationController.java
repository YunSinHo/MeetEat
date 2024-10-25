package com.example.demo.user.reservation;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.owner.OwnerService;
import com.example.demo.owner.store.StoreCombineDTO;

import java.util.List;
import java.util.Arrays;
@Controller
@RequestMapping("/reservation")
public class ReservationController {

    private final OwnerService ownerService;
    private final ReservationService reservationService;

    public ReservationController(OwnerService ownerService, ReservationService reservationService) {
        this.ownerService = ownerService;
        this.reservationService = reservationService;
    }

    @GetMapping("/main")
    public String mainReservation(Model model) {
        List<StoreCombineDTO> stores = reservationService.getStoreInformation();
        
        model.addAttribute("stores", stores);
        return "user/reservation/main";
    }

}
