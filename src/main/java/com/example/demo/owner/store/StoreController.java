package com.example.demo.owner.store;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.owner.Owner;
import com.example.demo.owner.OwnerService;
import com.example.demo.owner.store.image.StoreImage;
import com.example.demo.user.Users;
import com.example.demo.user.profile.image.UserProfileImage;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.PostMapping;

@Controller
@Transactional
@RequestMapping("/store")
public class StoreController {

    private final StoreService storeService;
    private final OwnerService ownerService;

    public StoreController(StoreService storeService, OwnerService ownerService) {
        this.storeService = storeService;
        this.ownerService = ownerService;
    }

    @ModelAttribute
    public String storeModel(Model model) throws IOException {
        Long ownerId = ownerService.getLoggedInOwnerId();
        Store store = storeService.findByOwnerId(ownerId);
        if (store == null) {
            return "redirect:/store/set-store";  // 리다이렉트 경로를 반환
        }
        model.addAttribute("store", store);

        return "";

    }

    @GetMapping("/set-store")
    public String setStore() {

        return "owner/store/store-information";
    }

    // 가게 저장
    @PostMapping("/basic/set-store")
    public String setStoreBasic(@ModelAttribute StoreDTO storeDTO) {
        Long ownerId = ownerService.getLoggedInOwnerId();

        Store store = new Store(ownerId, storeDTO);
        storeService.saveStore(store);

        return "redirect:/owner-bank/form-set";
    }

    // 가게 이미지 폼
    @GetMapping("/image/form")
    public String imageForm(Model model) {
        Long ownerId = ownerService.getLoggedInOwnerId();
        List<StoreImage> images = storeService.findByOwnerIdFromImage(ownerId);

        // 메인 이미지와 서브 이미지 나누기 (isMain 필드로 구분)
        StoreImage mainImage = new StoreImage();

        List<StoreImage> subImages = new ArrayList<>();
        for (StoreImage image : images) {
            if (image.getIsMain())
                mainImage = image;
            else
                subImages.add(image);
        }

        // 비어있는 이미지 슬롯은 noImage를 넣어준다
        if (mainImage.getImagePath() == null)
            mainImage = new StoreImage(
                    0L, ownerId, "/images/store/noImage.png", "noImage.png", true);

        int initialSubImageSize = subImages.size();
        for (int i = 0; i < 4 - initialSubImageSize; i++) {
            subImages.add(new StoreImage(
                    0L, ownerId, "/images/store/noImage.png", "noImage.png", false));
        }

        model.addAttribute("mainImage", mainImage);
        model.addAttribute("subImages", subImages);
        return "owner/store/store-picture";
    }

    // 가게 이미지 저장
    @PostMapping("/image/set-store")
    public String saveUserProfileImages(@RequestParam(value = "mainImage", required = false) MultipartFile mainImage,
            @RequestParam(value = "prevMainImage", required = false) String prevMainImage,
            @RequestParam(value = "images", required = false) MultipartFile[] images,
            @RequestParam(value = "prevImages", required = false) String[] prevImages,
            Model model) {
        String uploadDir = "/images/store/";
        Long ownerId = ownerService.getLoggedInOwnerId();

        // 서브 이미지 파일 처리
        for (int i = 0; i < prevImages.length; i++) {
            // images 배열에서 빈 값이나 null 체크
            if (images.length > i && images[i] != null && !images[i].isEmpty()) {
                // 새 파일로 업데이트
                // 기존 파일 삭제
                if (!prevImages[i].equals("noImage.png"))
                    deleteImage(uploadDir + prevImages[i]);
                // DB 데이터 삭제
                storeService.deleteByImageName(prevImages[i]);

                saveImage(images[i], ownerId, uploadDir, false);
            }
        }
        if (mainImage != null && !mainImage.isEmpty()) {
            // 새 파일이 업로드되면 기존 파일 삭제
            if (!prevMainImage.equals("noImage.png"))
                deleteImage(uploadDir + prevMainImage);

            storeService.deleteByImageName(prevMainImage);

            saveImage(mainImage, ownerId, uploadDir, true);
        }

        List<StoreImage> image = storeService.findByOwnerIdFromImage(ownerId);

        return "redirect:/login/owner/main";
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
            storeService.saveStoreImage(userId, newFileName, "/images/store/" + newFileName, isMain);
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

}
