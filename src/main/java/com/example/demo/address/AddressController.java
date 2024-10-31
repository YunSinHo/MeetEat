package com.example.demo.address;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    // 예시: Spring Boot에서 Geocoding API 호출 예제
    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://naveropenapi.apigw.ntruss.com")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .defaultHeader("X-NCP-APIGW-API-KEY-ID", "nix6e3syt1")
            .defaultHeader("X-NCP-APIGW-API-KEY", "Rx0M6mbtwdQTdLbilzSOKO3mAXuUc5uCe0iRc5lk")
            .build();

    @GetMapping("/geocode")
    @ResponseBody
    public List<Map<String, Object>> getGeocode(@RequestParam("address") String address) {
        System.out.println("입력된 주소: " + address);

        List<Map<String, Object>> results = new ArrayList<>();

        try {
            String encodedAddress = URLEncoder.encode(address, StandardCharsets.UTF_8);
            String response = webClient.get()
                    .uri(uriBuilder -> uriBuilder.path("/map-geocode/v2/geocode")
                            .queryParam("query", address)
                            .build())
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            JSONObject json = new JSONObject(response);
            // 결과 배열의 각 주소를 가져와 리스트에 추가
            if (json.has("addresses")) {
                for (int i = 0; i < json.getJSONArray("addresses").length(); i++) {
                    JSONObject addressInfo = json.getJSONArray("addresses").getJSONObject(i);

                    String roadAddress = addressInfo.optString("roadAddress", "도로명 주소 없음");
                    double lat = addressInfo.getDouble("y");
                    double lng = addressInfo.getDouble("x");

                    Map<String, Object> result = new HashMap<>();
                    result.put("roadAddress", roadAddress);
                    result.put("latitude", lat);
                    result.put("longitude", lng);

                    results.add(result);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return results;
    }

    // -------------------------유저-------------------------------
    // 주소 설정
    @GetMapping("/user/address-set")
    public String setAddress(Model model, @RequestParam(value = "goReserveMain", required = false) Boolean goReserveMain) {
        // 유저 주소 가져오기
        List<UserAddress> addresses = addressService.getUserAddress();
        model.addAttribute("addresses", addresses);
        model.addAttribute("goReserveMain", goReserveMain);

        return "user/address";
    }

    // 데이터베이스에 저장
    @PostMapping("/user/save-coordinates")
    @ResponseBody
    @Transactional
    public ResponseEntity<String> saveUserCoordinates(@RequestBody Map<String, String> coordinates) {

        System.out.println("위도 : " + coordinates.get("latitude"));
        System.out.println("경도 : " + coordinates.get("longitude"));
        System.out.println("주소명 : " + coordinates.get("name"));
        addressService.saveUserAddress(coordinates.get("latitude"), coordinates.get("longitude"),
                coordinates.get("name"));
        return ResponseEntity.ok("좌표가 저장되었습니다.");
    }

    // 주소 선택 이후 메인 페이지 이동
    @GetMapping("/user/update-map")
    public String updateMap(@RequestParam(value = "goReserveMain", required = false) Boolean goReserveMain) {
        if(goReserveMain) {
            return "redirect:/reservation/main";
        }
        return "redirect:/login/user/main";
    }

    // 기존 주소 활성화
    @PostMapping("/user/map/change-active")
    public String changeActiveUserMap(@RequestParam("lat") String lat, @RequestParam("lng") String lng, 
    @RequestParam("name") String name, @RequestParam("addressId") String addressId) {
        System.out.println("lat : " + lat );
        System.out.println("lng : " + lng);
        System.out.println("name : " + name);
        System.out.println("addressId : " + addressId);
        addressService.changeActiveUserMap(lat, lng, name, addressId);
        
        return "redirect:/login/user/main";
    }
    

}
