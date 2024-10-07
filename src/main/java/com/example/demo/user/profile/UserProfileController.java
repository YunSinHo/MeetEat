package com.example.demo.user.profile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.user.UserService;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;




@Controller
@RequestMapping("/profile")
public class UserProfileController {

    @Autowired
    private final UserService userService;
    @Autowired
    private final UserProfileService userProfileService;
    
    public UserProfileController(UserService userService, UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
        this.userService = userService;
    }


    // 회원가입시 이동되는 프로필설정 페이지
    @GetMapping("/join/form/set-user")
    public String setUserProfileForm() {
        return "/user/profile/basic-information";
    }

    // 기본 프로필 저장
    @PostMapping("/join/set-user")
    public String setUserProfile(@ModelAttribute UserProfileDTO userProfileDTO,
                                 @RequestParam("year") int year, 
                                 @RequestParam("month") int month, 
                                 @RequestParam("day") int day,
                                 Model model) {
                                    
        Long userId = userService.getLoggedInUserId();
        LocalDateTime dateTime = LocalDateTime.of(year, month, day, 0, 0); // datetime 형식으로 변환
        UserProfile profile = new UserProfile(userId, userProfileDTO, dateTime);
        userProfileService.saveBasicProfile(profile);

        model.addAttribute("profile", profile);
        
        return "/user/profile/personal-interest";
    }

    // interest(흥미) 저장
    @PostMapping("/interest/set-user")
    public String setUserInterest(@RequestParam("interest") List<String> interests) {

        for(String interest : interests) {
            Long id = userProfileService.findByNameFromInterest(interest);
            userProfileService.saveUserInterest(id,userService.getLoggedInUserId());
        }

        return "/user/profile/food-interest";
    }
    
    
    

    
}
