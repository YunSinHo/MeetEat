package com.example.demo.address;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.user.UserService;

@Service
public class AddressService {

    private final UserService userService;
    private final UserAddressRepository userAddressRepository;

    public AddressService(UserService userService, UserAddressRepository userAddressRepository){
        this.userService = userService;
        this.userAddressRepository = userAddressRepository;
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
        
        List<UserAddress> addresses = userAddressRepository.findAllByUserId(userId); // 활성화된 주소 변경
        for(UserAddress address : addresses) {
            if(address.getIsActive() == true) {
                address.setIsActive(false);
                userAddressRepository.save(address);
                break;
            }
        }

        Double latValue = Double.parseDouble(lat);
        Double lngValue = Double.parseDouble(lng);

        newAddress.setUserId(userId);
        newAddress.setLat(latValue);
        newAddress.setLng(lngValue);
        newAddress.setName(name);
        newAddress.setIsActive(true);
        userAddressRepository.save(newAddress);

       

    }

}
