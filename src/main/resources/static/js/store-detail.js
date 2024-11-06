

const rank = document.getElementById("rank").textContent;
function fillStars(rating) {
    const stars = document.querySelectorAll('.star');
    stars.forEach(star => {
        const starIndex = parseInt(star.getAttribute('data-index'), 10);

        // 별을 반만 채워야 할 경우
        if (rating >= starIndex) {
            star.classList.add('filled');
            star.style.background = '';
        } else if (rating >= starIndex - 0.5) {
            star.classList.add('filled');
            star.style.background = 'linear-gradient(to right, gold 50%, #ccc 50%)';
            star.style.webkitBackgroundClip = 'text';
            star.style.webkitTextFillColor = 'transparent';
        } else {
            star.classList.remove('filled');
            star.style.background = '';
        }
    });
}

fillStars(parseFloat(rank));


let currentIndex = 0;

function showSlide(index) {
    const slides = document.querySelectorAll('.slide');
    const totalSlides = slides.length;

    if (index >= totalSlides) currentIndex = 0;
    if (index < 0) currentIndex = totalSlides - 1;

    const offset = -currentIndex * 100;
    document.querySelector('.slider').style.transform = `translateX(${offset}%)`;
}

function moveSlide(direction) {
    currentIndex += direction;
    showSlide(currentIndex);
}

setInterval(() => {
    moveSlide(1); // 일정 시간이 지나면 자동으로 다음 슬라이드로 이동
}, 5000);


let currentIndex2 = 0; // 현재 인덱스
const itemsPerPage = 4; // 페이지당 요소 수
const reservationTimes = JSON.parse(document.getElementById('reservationTimes').value); // 서버에서 받은 시간대 배열

function renderTimes() {
    const timeContainer = document.getElementById('timeContainer');
    const totalItems = reservationTimes.length;

    // 이전 요소들을 제거
    timeContainer.innerHTML = '';

    // 현재 인덱스에 따라 5개 요소 추가
    for (let i = currentIndex2; i < Math.min(currentIndex2 + itemsPerPage, totalItems); i++) {
        const p = document.createElement('p');
        const div = document.createElement('div');
        div.classList.add('time');
        div.id = 'time' + i;
        p.innerText = reservationTimes[i]; // 시간대 추가
        p.style.textAlign = 'center';
        p.style.fontSize = '31px';
        p.style.marginLeft = '22px';
        p.style.marginTop = '22px';
        p.classList.add('times');
        timeContainer.appendChild(div);
        div.appendChild(p);

    }

    // 버튼 가시성 조정
    document.getElementById('scrollLeft').style.display = currentIndex2 > 0 ? 'block' : 'none';
    document.getElementById('scrollRight').style.display = currentIndex2 + itemsPerPage < totalItems ? 'block' : 'none';
}

function handleScrollLeft() {
    if (currentIndex2 > 0) {
        currentIndex2 -= itemsPerPage; // 인덱스 감소
        renderTimes(); // 재렌더링
    }
}

function handleScrollRight() {
    const totalItems = reservationTimes.length;
    if (currentIndex2 + itemsPerPage < totalItems) {
        currentIndex2 += itemsPerPage; // 인덱스 증가
        renderTimes(); // 재렌더링
    }
}

// 초기 렌더링 호출
renderTimes();

// 시간 클릭시 서버에서 테이블 개수 가져오기
const times = document.getElementsByClassName("time");
for (let i = 0; i < times.length; i++) {
    let timeElement = document.getElementById("time" + i);
    if (timeElement) {
        timeElement.addEventListener('click', function () {
            let time = timeElement.querySelector("p").textContent; 
            clickTime(time); 
        });
    }
}

function clickTime(time) {
    let storeId = document.getElementById("storeId").value;
    let date = document.getElementById("date").value;
    if (date === null || date === "") {
        alert('날짜를 선택해주세요.');
        return;
    }
    else {
        $.ajax({
            url : "/reservation/table-information",
            type : "post",
            data : {storeId : storeId, date : date, time : time},
            success : function (tableMap){
                alert("4 : " + tableMap.fourTable);
            },
            error : function () {
                alert("서버 오류가 발생했습니다.");
            }


        });
    }
   
}