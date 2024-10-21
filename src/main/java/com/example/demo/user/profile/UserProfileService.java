package com.example.demo.user.profile;

import java.util.List;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.owner.store.Categories;
import com.example.demo.owner.store.CategoriesRepository;
import com.example.demo.user.Users;
import com.example.demo.user.profile.foodinterest.UserFoodCategory;
import com.example.demo.user.profile.foodinterest.UserFoodCategoryRepository;
import com.example.demo.user.profile.image.UserProfileImage;
import com.example.demo.user.profile.image.UserProfileImageRepository;
import com.example.demo.user.profile.interest.Interest;
import com.example.demo.user.profile.interest.InterestRepository;
import com.example.demo.user.profile.interest.UserInterest;
import com.example.demo.user.profile.interest.UserInterestRepository;

import jakarta.transaction.Transactional;


@Service
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final UserFoodCategoryRepository userFoodCategoryRepository;
    private final UserInterestRepository userInterestRepository;
    private final InterestRepository interestRepository;
    private final CategoriesRepository categoriesRepository;
    private final UserProfileImageRepository userProfileImageRepository;

    @Autowired
    public UserProfileService(UserFoodCategoryRepository userFoodCategoryRepository,
            UserInterestRepository userInterestRepository,
            UserProfileRepository userProfileRepository,
            InterestRepository interestRepository,
            CategoriesRepository categoriesRepository,
            UserProfileImageRepository userProfileImageRepository) {
        this.userProfileRepository = userProfileRepository;
        this.userFoodCategoryRepository = userFoodCategoryRepository;
        this.userInterestRepository = userInterestRepository;
        this.interestRepository = interestRepository;
        this.categoriesRepository = categoriesRepository;
        this.userProfileImageRepository = userProfileImageRepository;

    }

    // 기본 프로필 저장
    public void saveBasicProfile(UserProfile profile) {
        userProfileRepository.save(profile);
    }

    // id로 프로필 찾기
    public UserProfile findByUserIdFromBasicProfile(Long userId) {
        UserProfile profile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("profile not found with userId" + userId));

        return profile;

    }

    // 선택한 interest들 id값 가져오기
    public Long findByNameFromInterest(String interest) {
        Interest interest2 = interestRepository.findByName(interest)
                .orElseThrow(() -> new RuntimeException("Interest not found with name" + interest));

        return interest2.getInterestId();
    }

    // interest save
    public void saveUserInterest(Long interestId, Long loggedInUserId) {
        UserInterest userInterest = new UserInterest();

        Users user = new Users();
        user.setUserId(loggedInUserId);
        userInterest.setUser(user);

        Interest interest = new Interest();
        interest.setInterestId(interestId);
        userInterest.setInterest(interest);

        userInterestRepository.save(userInterest);
    }
    // 유저의 취향 가져오기
    public List<Long> findUserInterests(Users user) {
        List<UserInterest> interests = userInterestRepository.findAllByUser(user);
        
        List<Long> interestId = new ArrayList<>();
        for(UserInterest interest : interests) {
            interestId.add(interest.getInterest().getInterestId());
        }
        return interestId;
    }

    // 이전에 있었던 취향 삭제
    public void deleteByUserIdUserInterests(Users user) {
        List<UserInterest> interests = userInterestRepository.findAllByUser(user);
        userInterestRepository.deleteAll(interests);
    }

    // 선택한 음식 카테고리들 id값 가져오기
    public Long findByNameFromCategory(String food) {
        Categories category = categoriesRepository.findByName(food)
                .orElseThrow(() -> new RuntimeException("food not found with name" + food));

        return category.getCategoryId();
    }
    // 유저의 food category 가져오기
    public List<Long> findUserCategories(Users user) {
        List<UserFoodCategory> categories = userFoodCategoryRepository.findAllByUser(user);
        
        List<Long> categorytId = new ArrayList<>();
        for(UserFoodCategory category : categories) {
            categorytId.add(category.getCategory().getCategoryId());
        }
        return categorytId;
    }

    // food save
    public void saveUserFood(Long foodId, Long loggedInUserId) {
        UserFoodCategory userFoodCategory = new UserFoodCategory();

        Users user = new Users();
        user.setUserId(loggedInUserId);
        userFoodCategory.setUser(user);

        Categories category = new Categories();
        category.setCategoryId(foodId);
        userFoodCategory.setCategory(category);

        userFoodCategoryRepository.save(userFoodCategory);
    }

    // 이전에 있었던 음식 취향 삭제
    public void deleteByUserIdUserFoodInterests(Users user) {

        List<UserFoodCategory> categories = userFoodCategoryRepository.findAllByUser(user);
        userFoodCategoryRepository.deleteAll(categories);
    }

    // 유저 프로필 이미지 저장
    public void saveUserImage(Long userId, String imageName, String imagePath, boolean isMain) {
        UserProfileImage image = new UserProfileImage();
        image.setImageName(imageName);
        image.setImagePath(imagePath);
        Users user = new Users();
        user.setUserId(userId);
        image.setUser(user);
        image.setIsMain(isMain);
        userProfileImageRepository.save(image);
    }

    public List<UserProfileImage> findByUserIdFromUserImages(Users user) {
        List<UserProfileImage> images = userProfileImageRepository.findAllByUser(user);
        return images;
    }

    // 기본 프로필 존재유무
    public boolean isExistBasicProfile(Long userId) {
        boolean isExist = userProfileRepository.existsByUserId(userId);
        return isExist;
    }

    // 기존 프로필 삭제
    public void deleteByImageName(String imageName) {
        userProfileImageRepository.deleteByImageName(imageName);
    }

    // db 취미 목록 가져오기
    public List<Interest> findAllInterest() {
        List<Interest> interests = interestRepository.findAll();

        return interests;
    }

    // db 음식 카테고리 가져오기
    public List<Categories> findAllCategory() {
        List<Categories> categories = categoriesRepository.findAll();

        return categories;
    }

   


}
