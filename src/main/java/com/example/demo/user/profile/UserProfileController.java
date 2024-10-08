package com.example.demo.user.profile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.user.UserService;
import com.example.demo.user.Users;
import com.example.demo.user.profile.image.UserProfileImage;

import jakarta.transaction.Transactional;

import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


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

    // 기본프로필 Model add
    public void addBasicProfile(Model model) {
        Long userId = userService.getLoggedInUserId();
        UserProfile profile = userProfileService.findByUserIdFromBasicProfile(userId);
        if (profile != null) {
            model.addAttribute("profile", profile);
        }

    }

    // 회원가입시 이동되는 프로필설정 페이지
    @GetMapping("/join/form/set-user")
    public String setUserProfileForm() {
        // System.out.println(userService.getLoggedInUserId());
        System.out.println("폼에서 유저 아이디" + SecurityContextHolder.getContext().getAuthentication().getName());

        System.out.println(SecurityContextHolder.getContext().getAuthentication().isAuthenticated());
        return "/user/profile/basic-information";
    }

    // 기본 프로필 저장
    @Transactional
    @PostMapping("/join/set-user")
    public String setUserProfile(@ModelAttribute UserProfileDTO userProfileDTO,
            @RequestParam("year") int year,
            @RequestParam("month") int month,
            @RequestParam("day") int day,
            Model model) {
        System.out.println("저장하는 동안" + SecurityContextHolder.getContext().getAuthentication().getName());
        Long userId = userService.getLoggedInUserId();
        LocalDateTime dateTime = LocalDateTime.of(year, month, day, 0, 0); // datetime 형식으로 변환
        UserProfile profile = new UserProfile(userId, userProfileDTO, dateTime);
        userProfileService.saveBasicProfile(profile);
        model.addAttribute("profile", profile);
        return "/user/profile/personal-interest";
    }

    // interest 폼
    @GetMapping("/interest/form")
    public String interestForm(Model model) {
        addBasicProfile(model);
        return "/user/profile/personal-interest";
    }

    // interest(흥미) 저장
    @PostMapping("/interest/set-user")
    public String setUserInterest(@RequestParam("interest") List<String> interests, Model model) {
        Long userId = userService.getLoggedInUserId();
        // 이전에 있었던 흥미삭제
        Users user = userService.findById(userId);
        userProfileService.deleteByUserIdUserInterests(user);

        for (String interest : interests) {
            Long id = userProfileService.findByNameFromInterest(interest);
            userProfileService.saveUserInterest(id, userService.getLoggedInUserId());
        }
        addBasicProfile(model);
        return "redirect:/profile/food/form";
    }

    // food-category 폼
    @GetMapping("/food/form")
    public String foodForm(Model model) {
        addBasicProfile(model);
        return "/user/profile/food-interest";
    }

    // 음식 취향 저장
    @PostMapping("/food/set-user")
    public String setUserFood(@RequestParam("food") List<String> foods, Model model) {
        Long userId = userService.getLoggedInUserId();
        // 이전에 있었던 음식 취향 삭제
        Users user = userService.findById(userId);
        userProfileService.deleteByUserIdUserFoodInterests(user);

        for (String food : foods) {
            Long id = userProfileService.findByNameFromCategory(food);
            userProfileService.saveUserFood(id, userService.getLoggedInUserId());
        }
        addBasicProfile(model);
        return "redirect:/profile/image/form";
    }

    // 유저 이미지 폼
    @GetMapping("/image/form")
    public String imageForm(Model model) {
        Long userId = userService.getLoggedInUserId();
        Users user = userService.findById(userId);
        List<UserProfileImage> images = userProfileService.findByUserIdFromUserImages(user);

        // 메인 이미지와 서브 이미지 나누기 (isMain 필드로 구분)
        UserProfileImage mainImage = null;

        List<UserProfileImage> subImages = new ArrayList<>();
        for (UserProfileImage image : images) {
            if (image.getIsMain())
                mainImage = image;
            else
                subImages.add(image);
        }
        model.addAttribute("mainImage", mainImage);
        model.addAttribute("subImages", subImages);
        addBasicProfile(model);
        return "/user/profile/profile-picture";
    }

    // 유저 이미지 저장
    @PostMapping("/image/set-user")
    public String handleFileUpload(@RequestParam("images") List<MultipartFile> images,
            @RequestParam("mainImage") MultipartFile mainImage,
            Model model) {
        String uploadDir = "src/main/resources/static/images/user/";
        Long userId = userService.getLoggedInUserId();

        // 서브 이미지 파일 처리
        for (MultipartFile image : images) {
            if (!image.isEmpty()) {
                saveImage(image, userId, uploadDir, false);
            }
        }

        // 메인 이미지 처리
        if (!mainImage.isEmpty()) {
            saveImage(mainImage, userId, uploadDir, true);
        }

        Users user = userService.findById(userId);
        List<UserProfileImage> image = userProfileService.findByUserIdFromUserImages(user);
        model.addAttribute("images", image);

        return "user/main";
    }

    // 이미지 저장 메소드
    private void saveImage(MultipartFile image, Long userId, String uploadDir, boolean isMain) {
        try {
            // 원래 파일 이름 및 확장자 추출
            String originalFileName = image.getOriginalFilename();
            String extension = originalFileName.substring(originalFileName.lastIndexOf("."));

            // UUID로 새로운 파일 이름 생성
            String uuid = UUID.randomUUID().toString();
            String newFileName = uuid + extension;

            // 파일 저장 경로 설정
            Path path = Paths.get(uploadDir + newFileName);
            Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            // 이미지 정보 저장 (isMain 값에 따라 메인 이미지 여부 구분)
            userProfileService.saveUserImage(userId, newFileName, "/images/user/" + newFileName, isMain);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // // 유저 이미지 저장
    // @PostMapping("/image/set-user")
    // public String handleFileUpload(@RequestParam("images") List<MultipartFile>
    // images,
    // @RequestParam("mainImage") MultipartFile mainImage, Model model) {
    // // 저장 경로 지정 (예: static/user-images)
    // String uploadDir = "src/main/resources/static/images/user/";
    // Long userId = userService.getLoggedInUserId();

    // // 서브 이미지 파일 처리
    // for (MultipartFile image : images) {
    // if (!image.isEmpty()) {
    // try {
    // // 원래 파일 이름
    // String originalFileName = image.getOriginalFilename();

    // // 파일 확장자 추출
    // String extension =
    // originalFileName.substring(originalFileName.lastIndexOf("."));

    // // UUID로 새로운 파일 이름 생성
    // String uuid = UUID.randomUUID().toString();
    // String newFileName = uuid + extension;

    // // 저장 경로
    // Path path = Paths.get(uploadDir + newFileName);
    // // 파일 저장
    // Files.copy(image.getInputStream(), path,
    // StandardCopyOption.REPLACE_EXISTING);

    // userProfileService.saveUserImage(userId, newFileName, "/images/user/" +
    // newFileName);
    // } catch (IOException e) {
    // e.printStackTrace();
    // }
    // }
    // }
    // // 메인 이미지
    // try {
    // String originalFileName = mainImage.getOriginalFilename();

    // // 파일 확장자 추출
    // String extension =
    // originalFileName.substring(originalFileName.lastIndexOf("."));

    // // UUID로 새로운 파일 이름 생성
    // String uuid = UUID.randomUUID().toString();
    // String newFileName = uuid + extension;

    // // 저장 경로
    // Path path = Paths.get(uploadDir + newFileName);
    // // 파일 저장
    // Files.copy(mainImage.getInputStream(), path,
    // StandardCopyOption.REPLACE_EXISTING);

    // userProfileService.saveUserImage(userId, newFileName, "/images/user/" +
    // newFileName, true);
    // } catch (IOException e) {
    // e.printStackTrace();
    // }
    // Users user = userService.findById(userId);
    // List<UserProfileImage> image =
    // userProfileService.findByUserIdFromUserImages(user);
    // model.addAttribute("images", image);
    // return "user/main";
    // }

}
