package com.example.demo.user.profile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.owner.store.Categories;
import com.example.demo.user.UserService;
import com.example.demo.user.Users;
import com.example.demo.user.profile.image.UserProfileImage;
import com.example.demo.user.profile.interest.Interest;

import jakarta.transaction.Transactional;

import org.springframework.web.bind.annotation.PostMapping;

import java.io.File;
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
@RequestMapping("/user-profile")
@Transactional
public class UserProfileController {

    @Autowired
    private final UserService userService;
    @Autowired
    private final UserProfileService userProfileService;

    public UserProfileController(UserService userService, UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
        this.userService = userService;
    }

    // 기본 프로필 model add
    @ModelAttribute
    public void addBasicProfile(Model model) {
        Long userId = userService.getLoggedInUserId();
        boolean isExistProfile = userProfileService.isExistBasicProfile(userId);
        if (!isExistProfile){
            System.out.println("프로필이 없는 사람");
            return;
        }
            
        UserProfile profile = userProfileService.findByUserIdFromBasicProfile(userId);
        if (profile != null) {
            model.addAttribute("profile", profile);
        }

    }

    @GetMapping("/set-user")
    public String setUserProfileForm() {

        return "user/profile/basic-information";
    }

    // 기본 프로필 저장
    @PostMapping("/basic/set-user")
    public String setUserProfile(@ModelAttribute UserProfileDTO userProfileDTO,
            @RequestParam("year") int year,
            @RequestParam("month") int month,
            @RequestParam("day") int day,
            Model model) {
        Long userId = userService.getLoggedInUserId();
        LocalDateTime dateTime = LocalDateTime.of(year, month, day, 0, 0); // datetime 형식으로 변환
        UserProfile profile = new UserProfile(userId, userProfileDTO, dateTime);
        userProfileService.saveBasicProfile(profile);
        return "redirect:/user-profile/interest/form";
    }

    // interest 폼
    @GetMapping("/interest/form")
    public String interestForm(Model model) {
        Long userId = userService.getLoggedInUserId();
        List<Interest> interests = userProfileService.findAllInterest();
        Users user = userService.findById(userId);
        List<Long> userInterestId = userProfileService.findUserInterests(user); 
        model.addAttribute("userInterestId", userInterestId);
        model.addAttribute("interests", interests);
        return "user/profile/personal-interest";
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
        return "redirect:/user-profile/food/form";
    }

    // food-category 폼
    @GetMapping("/food/form")
    public String foodForm(Model model) {
        Long userId = userService.getLoggedInUserId();
        Users user = userService.findById(userId);

        List<Categories> categories = userProfileService.findAllCategory();
        List<Long> userCategoryId = userProfileService.findUserCategories(user); 
        model.addAttribute("categories", categories);
        model.addAttribute("userCategoryId", userCategoryId);
        return "user/profile/food-interest";
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
        return "redirect:/user-profile/image/form";
    }

    // 유저 이미지 폼
    @GetMapping("/image/form")
    public String imageForm(Model model) {
        Long userId = userService.getLoggedInUserId();
        Users user = userService.findById(userId);
        List<UserProfileImage> images = userProfileService.findByUserIdFromUserImages(user);

        // 메인 이미지와 서브 이미지 나누기 (isMain 필드로 구분)
        UserProfileImage mainImage = new UserProfileImage();

        List<UserProfileImage> subImages = new ArrayList<>();
        for (UserProfileImage image : images) {
            if (image.getIsMain())
                mainImage = image;
            else
                subImages.add(image);
        }

        // 비어있는 이미지 슬롯은 noImage를 넣어준다
        if(mainImage.getImagePath() == null) mainImage = new UserProfileImage(
            0L, new Users() ,"/images/user/noImage.png", "noImage.png", true);

        int initialSubImageSize = subImages.size();
        for(int i = 0; i < 4 - initialSubImageSize; i++) {
            subImages.add(new UserProfileImage(
                0L, new Users() ,"/images/user/noImage.png", "noImage.png", false));
        }
        
        model.addAttribute("mainImage", mainImage);
        model.addAttribute("subImages", subImages);
        return "user/profile/profile-picture";
    }

    // 유저 이미지 저장
    @PostMapping("/image/set-user")
    public String saveUserProfileImages(@RequestParam(value = "mainImage", required = false) MultipartFile mainImage,
            @RequestParam(value = "prevMainImage", required = false) String prevMainImage,
            @RequestParam(value = "images", required = false) MultipartFile[] images,
            @RequestParam(value = "prevImages", required = false) String[] prevImages,
            Model model) {
        String uploadDir = "/images/user/";
        Long userId = userService.getLoggedInUserId();

        // 서브 이미지 파일 처리
        for (int i = 0; i < prevImages.length; i++) {
            // images 배열에서 빈 값이나 null 체크
            if (images.length > i && images[i] != null && !images[i].isEmpty()) {
                // 새 파일로 업데이트
                // 기존 파일 삭제
                if(!prevImages[i].equals("noImage.png"))
                deleteImage(uploadDir + prevImages[i]);
                // DB 데이터 삭제
                userProfileService.deleteByImageName(prevImages[i]);

                saveImage(images[i], userId, uploadDir, false);
            }
        }
        if (mainImage != null && !mainImage.isEmpty()) {
            // 새 파일이 업로드되면 기존 파일 삭제
            if(!prevMainImage.equals("noImage.png"))
            deleteImage(uploadDir + prevMainImage);

            userProfileService.deleteByImageName(prevMainImage);

            saveImage(mainImage, userId, uploadDir, true);
        }

        Users user = userService.findById(userId);
        List<UserProfileImage> image = userProfileService.findByUserIdFromUserImages(user);
        model.addAttribute("images", image);

        return "redirect:/login/user/main";
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
            Path path = Paths.get("src/main/resources/static" + uploadDir + newFileName);
            Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            // 이미지 정보 저장 (isMain 값에 따라 메인 이미지 여부 구분)
            userProfileService.saveUserImage(userId, newFileName, "/images/user/" + newFileName, isMain);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 이미지 삭제 메소드
    private void deleteImage(String filePath) {
        try {
            File file = new File("src/main/resources/static" + filePath);
            if (file.exists()) {
                if (file.delete()) {
                    System.out.println("File deleted successfully: " + filePath);
                } else {
                    throw new IOException("Failed to delete file: " + filePath);
                }
            } else {
                System.out.println("File not found: " + filePath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 푸터에 메인 이미지
    @PostMapping("/main-image")
    public ResponseEntity<String> mainImage(Model model) {
        String imagePath = userProfileService.getMainImage();
        if(imagePath == null) imagePath = "/images/user/noImage.png";
        // model.addAttribute("imagePath", imagePath);
        // System.out.println("유저 메인 이미지 : " + imagePath);
        return ResponseEntity.ok(imagePath);
    }
    

}
