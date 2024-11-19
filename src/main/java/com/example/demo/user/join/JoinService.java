package com.example.demo.user.join;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.owner.store.StoreService;
import com.example.demo.user.UserService;
import com.example.demo.user.profile.UserProfile;
import com.example.demo.user.profile.UserProfileService;

@Service
public class JoinService {

    private final StoreService storeService;
    private final UserProfileService userProfileService;
    private final UserService userService;
    private final JoinRequestRepo joinRequestRepo;
    

    private final JoinInfoRepo joinInfoRepo;
    public JoinService(StoreService storeService, UserProfileService userProfileService, JoinInfoRepo joinInfoRepo, UserService userService,
    JoinRequestRepo joinRequestRepo){
        this.storeService = storeService;
        this.userProfileService = userProfileService;
        this.userService = userService;
        this.joinInfoRepo = joinInfoRepo;
        this.joinRequestRepo = joinRequestRepo;
    }
    public void saveJoin(JoinInfoDTO info) {
        Long userId = userService.getLoggedInUserId();
        String userImage = userProfileService.getMainImage();

        JoinInfo newInfo = new JoinInfo();
        newInfo.setRequiredCount(info.getRequiredCount());
        newInfo.setUserImagePath(userImage);
        newInfo.setStoreImagePath(info.getStoreImagePath());

        newInfo.setCategory(info.getCategory());
        newInfo.setContent(info.getContent());
        newInfo.setUserCount(info.getUserCount());
        newInfo.setUserId(userId);
        newInfo.setStoreId(info.getStoreId());
        newInfo.setTitle(info.getTitle());
        newInfo.setStoreName(info.getStoreName());
        joinInfoRepo.save(newInfo);
    }
    public List<JoinInfo> findAll() {
        List<JoinInfo> infos = joinInfoRepo.findAll();

        return infos;

    }
    // 합석 메인화면 데이터 가져오기
    public List<JoinAndProfileDTO> findAllJoinAndProfile() {
         List<JoinInfo> infos = joinInfoRepo.findAll();
        List<JoinAndProfileDTO> jpList = new ArrayList<>();
        for(JoinInfo info : infos) {
            info.setUserCount(String.valueOf(Integer.valueOf(info.getUserCount()) + Integer.valueOf(info.getRequiredCount())));
            UserProfile profile = userProfileService.findByUserIdFromBasicProfile(info.getUserId());
            JoinAndProfileDTO jp = new JoinAndProfileDTO(profile,info);
            jpList.add(jp);
        }
        return jpList;
    }

    // 합석 글 디테일 화면
    public JoinAndProfileDTO findJoinAndProfile(Long id) {
        
        JoinInfo info = joinInfoRepo.findById(id).orElseThrow(()-> new RuntimeException("join not found"));
        UserProfile profile = userProfileService.findByUserIdFromBasicProfile(info.getUserId());
        if(profile.getGender() == 'M') profile.setGender('남');
        else profile.setGender('여');
        JoinAndProfileDTO jp = new JoinAndProfileDTO(profile, info);

        return jp;
    }

    // 연령대 구하기
    public String getAgeGroup(LocalDateTime dateOfBirth) {

        LocalDateTime now = LocalDateTime.now();
        
        int age = (int) ChronoUnit.YEARS.between(dateOfBirth, now);
        
        String ageGroup = (age / 10) + "0대";
        
        return ageGroup;
    }
    public List<JoinRequest> findAllByUserId(Long userId) {
        List<JoinRequest> requests = joinRequestRepo.findAllByUserId(userId);

        return requests;
    }
    public void saveRequest(Long joinId, Long userId) {
        Long otherId = userService.getLoggedInUserId();
        JoinRequest request = new JoinRequest();
        request.setOtherId(otherId);
        request.setJoinId(joinId);
        request.setUserId(userId);
        joinRequestRepo.save(request);
    }
    // 유효한 글의 합석 신청 여부
    public Boolean checkExistsRequest() {
        Long userId = userService.getLoggedInUserId();
        LocalDateTime currentTime = LocalDateTime.now();
        List<JoinInfo> infos = new ArrayList<>();
        infos = joinInfoRepo.findAllByUserIdAndEndDate(userId, currentTime);
        for(JoinInfo info : infos) {
            JoinRequest request = joinRequestRepo.findAllByJoinId(info.getJoinId());
            if(request != null && request.getIsRefused() != true && request.getIsAccept() != true)
                return true;
        }
        return false;
    }
    // endDate가 현재 시간을 넘지 않은 글 가져오기
    // public List<JoinInfo> findVaildJoin(){

    // }

    

    
}
