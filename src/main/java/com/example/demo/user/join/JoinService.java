package com.example.demo.user.join;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.owner.store.StoreService;
import com.example.demo.user.UserService;
import com.example.demo.user.profile.UserProfileService;

@Service
public class JoinService {

    private final StoreService storeService;
    private final UserProfileService userProfileService;
    private final UserService userService;
    

    private final JoinInfoRepo joinInfoRepo;
    public JoinService(StoreService storeService, UserProfileService userProfileService, JoinInfoRepo joinInfoRepo, UserService userService){
        this.storeService = storeService;
        this.userProfileService = userProfileService;
        this.userService = userService;
        this.joinInfoRepo = joinInfoRepo;
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

    

    
}
