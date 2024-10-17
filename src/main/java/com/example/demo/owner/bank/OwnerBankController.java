package com.example.demo.owner.bank;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.owner.OwnerService;
import com.example.demo.owner.store.Store;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@Controller
@RequestMapping("/owner-bank")
public class OwnerBankController {
    
    private final OwnerService ownerService;
    private final OwnerBankService ownerBankService;

    public OwnerBankController(OwnerBankService ownerBankService, OwnerService ownerService){
        this.ownerBankService = ownerBankService;
        this.ownerService = ownerService;
    }

    @GetMapping("/form-set")
    public String setOwnerBank() {
        return "owner/set-bank";
    }
    // 은행 저장
    @PostMapping("/set")
    public String setBank(@ModelAttribute OwnerBankDTO ownerBankDTO) {
        Long onwerId = ownerService.getLoggedInOwnerId();
        OwnerBank bank = new OwnerBank(onwerId, ownerBankDTO);

        ownerBankService.saveBank(bank);
        
        return "owner/store/store-picture";
    }
    
    

}
