package com.example.demo.reservation;

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
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping("/reservation")
public class ReservationController {

    private final OwnerService ownerService;
    private final ReservationService reservationService;
    private final AddressService addressService;
    private final ManagementService managementService;

    public ReservationController(OwnerService ownerService, ReservationService reservationService,
            AddressService addressService,
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

        return "user/reservation/main";
    }

    // 카테고리별 정렬
    @GetMapping("/sort")
    public String sortByCategory(@RequestParam("category") String category, Model model) {
        if (category.equals("전체"))
            return "redirect:/reservation/main";
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
        List<StoreCombineDTO> stores = reservationService.getStoreInformation(category);
        model.addAttribute("stores", stores);
        return "user/reservation/main";
    }

    // 가게 클릭시 내부 페이지

    @PostMapping("/store-detail/form")
    public String storeDetailForm(@RequestParam("storeId") String storeId, Model model) {
        Long id = Long.parseLong(storeId);
        StoreCombineDTO store = reservationService.getStoreInformation(id);
        List<String> reservationTimes = reservationService.getReservationTime(store.getStartTime(), store.getEndTime());
        // ObjectMapper를 사용하여 List<String>을 JSON 문자열로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        String reservationTimesJson = null;
        try {
            reservationTimesJson = objectMapper.writeValueAsString(reservationTimes);
        } catch (JsonProcessingException e) {
            e.printStackTrace(); // 예외 처리
        }

        System.out.println("사진 개수 : " + store.getStoreImages().size());
        model.addAttribute("reservationTimes", reservationTimesJson); // JSON 문자열을 모델에 추가
        model.addAttribute("store", store);

        return "user/reservation/store-detail";
    }
    @PostMapping("/choice-date")
    public String postMethodName(@RequestParam("date") String date) {
        System.out.println("선택한 날짜: " + date);
        // 여기서 날짜를 저장하거나 필요한 로직을 추가
        return "success"; // 프론트에 성공 메시지를 반환
    }
    
    

}
