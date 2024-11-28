let map; 
function initMap() {
    console.log("initMap 함수가 호출되었습니다."); 
    if (typeof naver !== "undefined" && typeof naver.maps !== "undefined") {
        const mapOptions = {
            center: new naver.maps.LatLng(37.737721, 127.0461758),
            zoom: 17
        };

        map = new naver.maps.Map('map', mapOptions); 
        console.log("지도 초기화 완료:", map); 

        // 지도가 초기화된 후 좌표가 있는 경우에만 업데이트
        let lat = document.getElementById("lat").value;
        let lng = document.getElementById("lng").value;
        if (lat && lng) {
            updateMapCenter(lat, lng);
        }

        // 지도 클릭 이벤트 리스너 추가
        naver.maps.Event.addListener(map, 'click', function (e) {
            const lat = e.coord.lat();
            const lng = e.coord.lng();
            console.log("클릭한 위치의 좌표:", lat, lng);
        });
    } else {
        console.error("네이버 지도 API가 로드되지 않았습니다.");
    }
}

// 주소 검색 결과로 지도 위치를 업데이트하는 함수
function updateMapCenter(latitude, longitude) {
    if (map) {
        const newCenter = new naver.maps.LatLng(latitude, longitude);
        map.setCenter(newCenter);
        map.setZoom(17);
        
        console.log("지도 중심이 업데이트되었습니다:", latitude, longitude);
    } else {
        console.error("지도 초기화가 완료되지 않았습니다.");
    }
}



fetch('/map.html')
    .then(response => response.text())
    .then(data => {
        document.getElementById('map_template').innerHTML = data;
        console.log("map.html이 성공적으로 로드되었습니다.");
        
        // map.html의 스크립트를 실행
        const script = document.createElement('script');
        script.src = 'https://oapi.map.naver.com/openapi/v3/maps.js?ncpClientId=y614hn2kbc';
        script.onload = initMap; // initMap 호출
        document.body.appendChild(script); // 스크립트를 body에 추가
    })
    .catch(error => console.error("map.html 로드 중 오류 발생:", error));

    