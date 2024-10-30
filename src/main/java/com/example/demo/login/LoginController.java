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
import com.example.demo.user.profile.UserProfileService;

import java.util.List;
@Controller
@RequestMapping("/login")
public class LoginController {

    private final UserService userService;
    private final UserProfileService userProfileService;
    private final OwnerService ownerService;
    private final StoreService storeService;
    private final AddressService addressService;

    public LoginController(UserService userService, UserProfileService userProfileService,
            OwnerService ownerService, StoreService storeService, AddressService addressService) {
        this.userService = userService;
        this.userProfileService = userProfileService;
        this.ownerService = ownerService;
        this.storeService = storeService;
        this.addressService = addressService;
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
    @Transactional
    public String userMain(Model model) {
        Long userId = userService.getLoggedInUserId();
        boolean isExistProfile = userProfileService.isExistBasicProfile(userId);
        Double latValue = null;
        Double lngValue = null;
        String name = null;
        
        List<UserAddress> addresses = addressService.getUserAddress();
        for(UserAddress address : addresses) {
            if(address.getIsActive() == true) {
                latValue = address.getLat();
                lngValue = address.getLng();
                name = address.getName();
                break;
            }
        }
        
        
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
