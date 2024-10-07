package com.example.demo.user.profile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.user.Users;
import com.example.demo.user.profile.foodinterest.UserFoodCategoryRepository;
import com.example.demo.user.profile.interest.Interest;
import com.example.demo.user.profile.interest.InterestRepository;
import com.example.demo.user.profile.interest.UserInterest;
import com.example.demo.user.profile.interest.UserInterestRepository;

@Service
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final UserFoodCategoryRepository userFoodCategoryRepository;
    private final UserInterestRepository userInterestRepository;
    private final InterestRepository interestRepository;

    @Autowired
    public UserProfileService(UserFoodCategoryRepository userFoodCategoryRepository,
            UserInterestRepository userInterestRepository,
            UserProfileRepository userProfileRepository,
            InterestRepository interestRepository) {
        this.userProfileRepository = userProfileRepository;
        this.userFoodCategoryRepository = userFoodCategoryRepository;
        this.userInterestRepository = userInterestRepository;
        this.interestRepository = interestRepository;

    }

    // 기본 프로필 저장
    public void saveBasicProfile(UserProfile profile) {
        userProfileRepository.save(profile);
    }

    // id로 프로필 찾기
    public UserProfile findByUserIdFromBasicProfile(Long userId) {
        UserProfile profile = userProfileRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("profile not found with userId" + userId));

        return profile;

    }

    // 선택한 interest들 id값 가져오기
    public Long findByNameFromInterest(String interest) {
        Interest interest2 = interestRepository.findByName(interest)
                .orElseThrow(() -> new RuntimeException("Interest not found with name" + interest));

        return interest2.getInterestId();
    }

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

}
