package com.example.demo.reservation;

import org.springframework.stereotype.Controller;

@Controller
public class DateController {
    
    private final DateService dateService;

    public DateController(DateService dateService) {
        this.dateService = dateService;
    }

    
}
