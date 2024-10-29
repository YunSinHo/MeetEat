function initMap() {
    console.log("initMap 함수가 호출되었습니다."); // 추가된 로그
    if (typeof naver !== "undefined") {
        var mapOptions = {
            center: new naver.maps.LatLng(37.3595704, 127.105399),
            zoom: 10
        };

        var map = new naver.maps.Map('map', mapOptions);
        console.log("지도 초기화 완료."); // 추가된 로그
    } else {
        console.error("네이버 지도 API가 로드되지 않았습니다.");
    }
}

fetch('/map.html')
    .then(response => response.text())
    .then(data => {
        document.getElementById('map_template').innerHTML = data;
        console.log("map.html이 성공적으로 로드되었습니다.");
        
        // map.html의 스크립트를 실행
        const script = document.createElement('script');
        script.src = 'https://oapi.map.naver.com/openapi/v3/maps.js?ncpClientId=hmuxpcm6uv';
        script.onload = initMap; // initMap 호출
        document.body.appendChild(script); // 스크립트를 body에 추가
    })
    .catch(error => console.error("map.html 로드 중 오류 발생:", error));
