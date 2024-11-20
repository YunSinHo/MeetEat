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

    public JoinService(StoreService storeService, UserProfileService userProfileService, JoinInfoRepo joinInfoRepo,
            UserService userService,
            JoinRequestRepo joinRequestRepo) {
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

        List<JoinInfo> infos = new ArrayList<>();
        LocalDateTime currDateTime = LocalDateTime.now();
        infos = joinInfoRepo.findAllByEndDateAndIsAccept(false, currDateTime);
        // List<JoinInfo> infos = joinInfoRepo.findAll();

        List<JoinAndProfileDTO> jpList = new ArrayList<>();
        for (JoinInfo info : infos) {
            info.setUserCount(
                    String.valueOf(Integer.valueOf(info.getUserCount()) + Integer.valueOf(info.getRequiredCount())));
            UserProfile profile = userProfileService.findByUserIdFromBasicProfile(info.getUserId());
            JoinAndProfileDTO jp = new JoinAndProfileDTO(profile, info);
            jpList.add(jp);
        }
        return jpList;
    }

    // 합석 글 디테일 화면
    public JoinAndProfileDTO findJoinAndProfile(Long id) {

        JoinInfo info = joinInfoRepo.findById(id).orElseThrow(() -> new RuntimeException("join not found"));
        UserProfile profile = userProfileService.findByUserIdFromBasicProfile(info.getUserId());
        if (profile.getGender() == 'M')
            profile.setGender('남');
        else
            profile.setGender('여');
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
        request.setOtherImagePath(userProfileService.getMainImage());
        joinRequestRepo.save(request);
    }

    // 유효한 글의 합석 신청 여부
    public Boolean checkExistsRequest() {

        List<JoinInfo> infos = new ArrayList<>();
        infos = findVaildJoin();
        for (JoinInfo info : infos) {
            JoinRequest request = joinRequestRepo.findByJoinId(info.getJoinId());
            if (request != null && request.getIsRefused() != true && request.getIsAccept() != true)
                return true;
        }
        return false;
    }

    public List<JoinRequest> findAllByOtherId(Long userId) {
        List<JoinRequest> requests = joinRequestRepo.findAllByOtherId(userId);

        return requests;
    }

    // 수락하지 않고 endDate가 현재시간을 넘지 않은 글
    public List<JoinInfo> findVaildJoin() {
        Long userId = userService.getLoggedInUserId();
        LocalDateTime currentTime = LocalDateTime.now();
        List<JoinInfo> infos = new ArrayList<>();
        infos = joinInfoRepo.findAllByUserIdAndEndDate(userId, currentTime);
        List<JoinInfo> newInfos = new ArrayList<>();
        for (int i = 0; i < infos.size(); i++) {
            JoinRequest request = joinRequestRepo.findByJoinId(infos.get(i).getJoinId());
            if (request != null && request.getIsAccept() != true) {
                newInfos.add(infos.get(i));
            }
        }
        return newInfos;

    }

    public List<JoinInfo> findAcceptReqeust() {
        Long userId = userService.getLoggedInUserId();
        LocalDateTime currentTime = LocalDateTime.now();
        List<JoinInfo> infos = new ArrayList<>();
        infos.addAll(joinInfoRepo.findAllByUserIdAndEndDate(userId, currentTime));
        System.out.println(infos.size() + "1인포");
        infos.addAll(joinInfoRepo.findAllByOtherIdAndEndDate(userId, currentTime));
        System.out.println(infos.size() + "2인포");
        List<JoinInfo> newInfos = new ArrayList<>();
        for (int i = 0; i < infos.size(); i++) {
            if(infos.get(i).getIsAccept() == true)
            newInfos.add(infos.get(i));
        }
        System.out.println(newInfos.size() + "3인포");
        return newInfos;

    }

    public List<JoinAndRequestDTO> findVaildRequests(List<JoinInfo> infos) {

        List<JoinAndRequestDTO> jr = new ArrayList<>();
        for (JoinInfo info : infos) {
            List<JoinRequest> requests = joinRequestRepo.findAllByJoinIdAndIsAcceptAndIsRefused(info.getJoinId(), false,
                    false);

            JoinAndRequestDTO newJr = new JoinAndRequestDTO(requests, info);
            jr.add(newJr);
        }

        return jr;

    }

    public List<JoinAndRequestDTO> findAcceptJoin(List<JoinInfo> infos) {

        List<JoinAndRequestDTO> jr = new ArrayList<>();
        for (JoinInfo info : infos) {
            List<JoinRequest> requests = joinRequestRepo.findAllByJoinIdAndIsAcceptAndIsRefused(info.getJoinId(), true,
                    false);

            JoinAndRequestDTO newJr = new JoinAndRequestDTO(requests, info);
            jr.add(newJr);
        }

        return jr;

    }

    public void updateJoin(Long joinId, Long otherId, Boolean accept) {
        JoinInfo info = joinInfoRepo.findById(joinId).orElseThrow(() -> new RuntimeException("join을 찾을수 없습니다"));
        info.setIsAccept(accept);
        info.setOtherId(otherId);
        joinInfoRepo.save(info);

    }

    public void updateRequest(Long requestId, Boolean accept) {
        JoinRequest request = joinRequestRepo.findById(requestId)
                .orElseThrow(() -> new RuntimeException("request을 찾을수 없습니다"));
        if (accept) {
            request.setIsAccept(accept);
        } else {
            request.setIsRefused(accept);
        }
        joinRequestRepo.save(request);
    }

}
