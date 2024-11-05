

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

const today = new Date();
const dateContainer = document.getElementById('dates');
const monthDisplay = document.querySelector('.month');

// 현재 월 표시
monthDisplay.textContent = `${today.getMonth() + 1}월`;

// 요일 배열
const daysOfWeek = ['일', '월', '화', '수', '목', '금', '토'];

// 날짜 클릭 시 today 스타일 적용 함수
function selectDate(event) {
    // 모든 날짜 요소에서 'today' 클래스 제거
    document.querySelectorAll('.date-item').forEach(item => {
        item.classList.remove('today');
    });

    // 클릭한 요소에 'today' 클래스 추가
    event.currentTarget.classList.add('today');
}

// 오늘부터 5일간의 날짜와 요일을 생성
for (let i = 0; i < 5; i++) {
    const date = new Date(today);
    date.setDate(today.getDate() + i);

    const dateItem = document.createElement('div');
    dateItem.classList.add('date-item');
    if (i === 0) {
        dateItem.classList.add('today'); // 처음에는 오늘 날짜에 스타일 적용
    }

    const day = i === 0 ? '오늘' : daysOfWeek[date.getDay()];
    dateItem.innerHTML = `<div>${day}</div><div>${date.getDate()}</div>`;

    // 클릭 이벤트 리스너 추가
    dateItem.addEventListener('click', selectDate);

    dateContainer.appendChild(dateItem);
}
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

