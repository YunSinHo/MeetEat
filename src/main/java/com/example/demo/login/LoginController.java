package com.example.demo.login;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.address.AddressService;
import com.example.demo.address.UserAddress;
import com.example.demo.owner.OwnerService;
import com.example.demo.owner.store.StoreService;
import com.example.demo.user.UserService;
import com.example.demo.user.join.JoinService;
import com.example.demo.user.profile.UserProfileService;

import jakarta.servlet.http.HttpServletResponse;

import java.util.List;
@Controller
@RequestMapping("/login")
public class LoginController {

    private final UserService userService;
    private final UserProfileService userProfileService;
    private final OwnerService ownerService;
    private final StoreService storeService;
    private final AddressService addressService;
    private final JoinService joinService;

    public LoginController(UserService userService, UserProfileService userProfileService,
            OwnerService ownerService, StoreService storeService, AddressService addressService, JoinService joinService) {
        this.userService = userService;
        this.userProfileService = userProfileService;
        this.ownerService = ownerService;
        this.storeService = storeService;
        this.addressService = addressService;
        this.joinService = joinService;
    }

    @GetMapping("")
    public String login(@RequestParam(value = "error", required = false) Boolean error, Model model) {
        if (error == null) {
            error = false;
        }
        model.addAttribute("errorMessage", "아이디나 비밀번호가 틀렸습니다.");
        model.addAttribute("error", error);
        return "/login";
    }

    // -------------------------------------유저
    // 유저 로그인폼

    // 로그인 성공시 메인페이지로 이동
    @GetMapping("/user/main")
    public String userMain(Model model, HttpServletResponse response) {
        // 캐싱 방지
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);

        Long userId = userService.getLoggedInUserId();
        boolean isExistProfile = userProfileService.isExistBasicProfile(userId);
        Double latValue = null;
        Double lngValue = null;
        String name = null;
        try {
            Thread.sleep(100); // 100ms = 0.1초
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<UserAddress> addresses = addressService.getUserAddress();
        for(UserAddress address : addresses) {
            if(address.getIsActive() == true) {
                latValue = address.getLat();
                lngValue = address.getLng();
                name = address.getName();
                break;
            }
        }
        Boolean existsRequest = joinService.checkExistsRequest();
        model.addAttribute("existsRequest", existsRequest);
        
        System.out.println("1위도 : " + latValue) ;
        System.out.println("1경도 : " + lngValue) ;
        System.out.println("1주소명 : " +name ) ;
        // 기본 프로필이 존재하면 메인페이지로
        if (isExistProfile){
            model.addAttribute("lat", latValue);
            model.addAttribute("lng", lngValue);
            model.addAttribute("name", name);
            return "user/main";

        }

            

        return "redirect:/user-profile/set-user";
    }
   
    

    // ------------------------------------오너

    // 로그인 성공시 메인페이지로 이동
    @GetMapping("/owner/main")
    public String ownerMain() {
        System.out.println("로그인 성공(오너)");
        Long ownerId = ownerService.getLoggedInOwnerId();
        boolean isExistProfile = storeService.isExistProfile(ownerId);

        // 기본 프로필이 존재하면 메인페이지로
        if (isExistProfile) {
            return "/owner/main";
        }

        return "redirect:/store/set-store";
    }

}
