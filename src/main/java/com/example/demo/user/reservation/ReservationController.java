package com.example.demo.user.reservation;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/reservation")
public class ReservationController {
    

    @GetMapping("/main")
    public String mainReservation() {
        return "user/reservation/main";
    }
    
}
