package com.example.demo.login;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.owner.OwnerService;
import com.example.demo.owner.store.StoreService;
import com.example.demo.user.UserService;
import com.example.demo.user.profile.UserProfileService;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/login")
public class LoginController {

    private final UserService userService;
    private final UserProfileService userProfileService;
    private final OwnerService ownerService;
    private final StoreService storeService;

    public LoginController(UserService userService, UserProfileService userProfileService,
                           OwnerService ownerService, StoreService storeService){
        this.userService = userService;
        this.userProfileService = userProfileService;
        this.ownerService = ownerService;
        this.storeService = storeService;
    }

    @GetMapping("")
    public String login() {
        return "/login";
    }
    
    // -------------------------------------유저
    // 유저 로그인폼
    

    // 로그인 성공시 메인페이지로 이동
    @GetMapping("/user/main")
    public String userMain() {
        Long userId = userService.getLoggedInUserId();
        boolean isExistProfile = userProfileService.isExistBasicProfile(userId);

        // 기본 프로필이 존재하면 메인페이지로
        if(isExistProfile) return "/user/main";
        
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
        if(isExistProfile) {
            return "/owner/main";
        }
        
        return "redirect:/store/set-store";
    }

}
