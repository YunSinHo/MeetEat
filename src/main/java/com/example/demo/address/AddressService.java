package com.example.demo.address;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.owner.store.Store;
import com.example.demo.owner.store.StoreService;
import com.example.demo.user.UserService;

@Service
public class AddressService {

    private final UserService userService;
    private final UserAddressRepository userAddressRepository;
    private final StoreService storeService;

    public AddressService(UserService userService, UserAddressRepository userAddressRepository, StoreService storeService){
        this.userService = userService;
        this.userAddressRepository = userAddressRepository;
        this.storeService = storeService;
    }
    // 유저의 등록된 주소목록 가져오기
    public List<UserAddress> getUserAddress() {
        Long userId = userService.getLoggedInUserId();
        List<UserAddress> addresses = userAddressRepository.findAllByUserId(userId);
        return addresses;
    }
    // 유저 주소 저장
    public void saveUserAddress(String lat, String lng, String name) {
        
        UserAddress newAddress = new UserAddress();

        Long userId = userService.getLoggedInUserId();
        
        resetActiveMap();

        Double latValue = Double.parseDouble(lat);
        Double lngValue = Double.parseDouble(lng);

        newAddress.setUserId(userId);
        newAddress.setLat(latValue);
        newAddress.setLng(lngValue);
        newAddress.setName(name);
        newAddress.setIsActive(true);
        userAddressRepository.save(newAddress);
    }

    // 활성화된 주소 변경
    public void changeActiveUserMap(String lat, String lng, String name, String addressId) {
        UserAddress address = new UserAddress();
        resetActiveMap();
        Long userId = userService.getLoggedInUserId();
        
        address.setAddressId(Long.parseLong(addressId));
        address.setIsActive(true);
        address.setLat(Double.parseDouble(lat));
        address.setLng(Double.parseDouble(lng));
        address.setUserId(userId);
        address.setName(name);
        userAddressRepository.save(address);
    }

    // 활성화된 주소 초기화
    public void resetActiveMap(){
        Long userId = userService.getLoggedInUserId();
        
        List<UserAddress> addresses = userAddressRepository.findAllByUserId(userId); 
        for(UserAddress address : addresses) {
            if(address.getIsActive() == true) {
                address.setIsActive(false);
                userAddressRepository.save(address);
                break;
            }
        }
    }
    // 유저의 활성화된 주소 가져오기
    public UserAddress findActiveUserAddress() {
        Long userId = userService.getLoggedInUserId();
        Optional<UserAddress> address = userAddressRepository.findByUserIdAndIsActive(userId, true);

        return address.get();
    }
    public List<Map<String, Object>> mapMarking(List<Map<String, Object>> locInfos) {
        // 가게 위치 정보들 가져오기
        List<Store> stores = storeService.findAll();
        for(Store store : stores) {
            Map<String, Object> map = new HashMap<>();
            map.put("lat", store.getLat());
            map.put("lng", store.getLng());
            map.put("name", store.getStoreName());
            locInfos.add(map);
        }
        UserAddress address = findActiveUserAddress();
        Map<String, Object> map = new HashMap<>();
        map.put("lat", address.getLat());
        map.put("lng", address.getLng());
        map.put("name", "현재 위치");
        locInfos.add(map);
        System.out.println("유저 활성화된 주소 : " + address.getName());
        return locInfos;
    }
    public Map<String, Object> mapMarking(Map<String, Object> locInfos, Long storeId) {
        Store store = storeService.findById(storeId);

        locInfos.put("lat", store.getLat());
        locInfos.put("lng", store.getLng());
        locInfos.put("name", store.getStoreName());
        return locInfos;
    }

}
