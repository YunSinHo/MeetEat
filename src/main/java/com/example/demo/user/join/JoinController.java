package com.example.demo.user.join;

import java.time.LocalDate;
import java.util.ArrayList;
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
import com.example.demo.user.UserService;
import com.example.demo.user.Users;
import com.example.demo.user.profile.UserProfile;
import com.example.demo.user.profile.UserProfileService;
import com.example.demo.user.profile.image.UserProfileImage;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequestMapping("/join")
public class JoinController {

    private final ReservationService reservationService;
    private final AddressService addressService;
    private final JoinService joinService;
    private final StoreService storeService;
    private final UserProfileService userProfileService;
    private final UserService userService;

    public JoinController(ReservationService reservationService, AddressService addressService, JoinService joinService,
            StoreService storeService, UserProfileService userProfileService, UserService userService) {
        this.reservationService = reservationService;
        this.joinService = joinService;
        this.addressService = addressService;
        this.storeService = storeService;
        this.userProfileService = userProfileService;
        this.userService = userService;
    }

    @GetMapping("/main")
    public String main(Model model) {
        List<JoinAndProfileDTO> jpList = joinService.findAllJoinAndProfile();

        List<UserAddress> addresses = addressService.getUserAddress();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (UserAddress address : addresses) {
            if (address.getIsActive() == true) {
                model.addAttribute("address", address);
                break;
            }
        }
        if (addresses.isEmpty()) {
            UserAddress address = new UserAddress();
            address.setName("주소를 설정해주세요");
            model.addAttribute("address", address);
        }
        model.addAttribute("jp", jpList);
        model.addAttribute("isJoin", true);

        return "user/join/main";
    }

    @GetMapping("/store-select")
    public String storeSelect(@RequestParam(value = "isJoin", required = false) Boolean isJoin) {

        return "redirect:/reservation/main?isJoin=" + isJoin;
    }

    @PostMapping("/join-confirm/form")
    public String joinForm(Model model, @RequestParam("storeId") String storeId) {
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

    // 합석 디테일 페이지
    @PostMapping("/join-detail/form")
    public String joinDetailForm(@RequestParam("joinId") String joinId, Model model) {
        Long id = Long.valueOf(joinId);
        Long userId = userService.getLoggedInUserId();
        JoinAndProfileDTO jp = joinService.findJoinAndProfile(id);
        Users user = userService.findById(jp.getInfo().getUserId());
        List<UserProfileImage> images = userProfileService.findByUserIdFromUserImages(user);
        String ageGroup = joinService.getAgeGroup(jp.getProfile().getDateOfBirth());

        Boolean isRequested = false;
        List<JoinRequest> requests = new ArrayList<>();
        requests = joinService.findAllByOtherId(userId);
        System.out.println(requests.size() + "글개수");
        for (JoinRequest request : requests) {
            if (request.getJoinId() == id)
                isRequested = true;
        }
        model.addAttribute("isRequested", isRequested);
        model.addAttribute("userId", userId);
        model.addAttribute("ageGroup", ageGroup);
        model.addAttribute("images", images);
        model.addAttribute("jp", jp);

        return "user/join/join-detail";
    }

    @PostMapping("/request")
    public String request(@RequestParam("joinId") Long joinId, @RequestParam("userId") Long userId) {
        joinService.saveRequest(joinId, userId);

        return "redirect:/login/user/main";
    }

    @GetMapping("/status")
    public String status(Model model) {
        List<JoinInfo> infos = joinService.findVaildJoin();
        List<JoinAndRequestDTO> jr = joinService.findVaildRequests(infos);

        model.addAttribute("status", jr);
        return "user/join/status";
    }

    // 합석 여부 수락or거절 저장
    @PostMapping("/accept")
    public String accept(@RequestParam("accept") Boolean accept,
            @RequestParam("joinId") Long joinId, @RequestParam("requestId") Long requestId, @RequestParam("otherId") Long otherId) {

        joinService.updateJoin(joinId, otherId, accept);
        joinService.updateRequest(requestId, accept);
        return "redirect:/join/status";
    }

}
