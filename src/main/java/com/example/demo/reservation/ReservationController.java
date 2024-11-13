package com.example.demo.reservation;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.address.AddressService;
import com.example.demo.address.UserAddress;
import com.example.demo.owner.store.StoreCombineDTO;
import com.example.demo.owner.store.management.ManagementService;
import com.example.demo.owner.store.management.SRDService;
import com.example.demo.owner.store.management.menu.StoreMenu;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/reservation")
public class ReservationController {

    private final ReservationService reservationService;
    private final AddressService addressService;
    private final ManagementService managementService;
    private final DateService dateService;
    private final SRDService srdService;

    public ReservationController(SRDService srdService, ReservationService reservationService,
            AddressService addressService, DateService dateService,
            ManagementService managementService) {
        this.srdService = srdService;
        this.reservationService = reservationService;
        this.addressService = addressService;
        this.managementService = managementService;
        this.dateService = dateService;
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


        // 메뉴 데이터
        List<StoreMenu> menus = managementService.findAllStoreMenu(Long.parseLong(storeId));
        List<StoreMenu> mainMenu = new ArrayList<>();
        List<StoreMenu> subMenu = new ArrayList<>();
        if (menus.isEmpty())
            menus = new ArrayList<>();

        for (StoreMenu menu : menus) {
            if (menu.getIsMain() == true)
                mainMenu.add(menu);
            else
                subMenu.add(menu);
        }
        model.addAttribute("mainMenu", mainMenu);
        model.addAttribute("subMenu", subMenu);
        return "user/reservation/store-detail";
    }

    // 선택한 날짜 뷰에 저장
    @PostMapping("/choice-date")
    public ResponseEntity<String> choiceDate(@RequestParam("date") String date,
            @RequestParam("storeId") String storeId) {
        System.out.println("선택한 날짜: " + date + " id : " + storeId);
        return ResponseEntity.ok(date);
    }

    // 선택 시간대 테이블 정보
    @PostMapping("/table-information")
    public ResponseEntity<Map<String, Integer>> tableInformation(@RequestParam("storeId") String storeId,
            @RequestParam("date") String date,
            @RequestParam("time") String time) {
        Map<String, Integer> timeMap = dateService.getTableInformation(storeId, date, time);
        return ResponseEntity.ok(timeMap);
    }

    // 메뉴 가져오기
    @PostMapping("/choice-date/form")
    public ResponseEntity<String> menuForm(@RequestParam("storeId")String storeId, HttpSession session, Model model) {
        // session.setAttribute("ReservationBasicDTO", dto);
        System.out.println("아이디:" + storeId);
        List<StoreMenu> menus = managementService.findAllStoreMenu(Long.parseLong(storeId));
        List<StoreMenu> mainMenu = new ArrayList<>();
        List<StoreMenu> subMenu = new ArrayList<>();
        if (menus.isEmpty())
            menus = new ArrayList<>();

        for (StoreMenu menu : menus) {
            if (menu.getIsMain() == true)
                mainMenu.add(menu);
            else
                subMenu.add(menu);
        }
        model.addAttribute("mainMenu", mainMenu);
        model.addAttribute("subMenu", subMenu);

        return ResponseEntity.ok("success");
    }

    // 최종 결제 페이지
    @PostMapping("/payment/form")
    public String paymentForm(@RequestParam("storeMenuId") List<Long> storeMenuId,
            @RequestParam("number") List<String> number, HttpSession session, Model model) {
            
        ReservationBasicDTO reservationBasicDTO = (ReservationBasicDTO) session.getAttribute("ReservationBasicDTO");
        StoreCombineDTO store = reservationService
                .getStoreInformation(Long.parseLong(reservationBasicDTO.getStoreId()));
        model.addAttribute("reservation", reservationBasicDTO);
        model.addAttribute("store", store);
        return "user/reservation/payment";
    }

    // 예약 내역 db저장
    @PostMapping("/reservation-save")
    @Transactional
    public String saveReservation(@RequestParam("finalPayment") String finalPayment,
            @RequestParam("isJoin") String isJoin, HttpSession session) {
        ReservationBasicDTO reservationBasicDTO = (ReservationBasicDTO) session.getAttribute("ReservationBasicDTO");
        boolean isSave = reservationService.saveReservation(finalPayment, isJoin, reservationBasicDTO);
        srdService.updateReservationTable(reservationBasicDTO);
        return "redirect:/reservation/status";
    }

    // 예약 현황
    @GetMapping("/status")
    public String reservationStatus(Model model) {
        List<StoreReservationInfo> infos = reservationService.findVaildReservaion();
        model.addAttribute("status", infos);

        return "user/reservation/status";
    }
    

}
