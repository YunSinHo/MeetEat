

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

        // 클릭 이벤트 추가
        div.addEventListener('click', function () {
            clickTime(reservationTimes[i]);
        });

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
let prevTime;
let isExistPrev = false;
const times = document.getElementsByClassName("time");
for (let i = 0; i < times.length; i++) {
    let timeElement = document.getElementById("time" + i);

    if (timeElement) {
        timeElement.addEventListener('click', function () {
            if (isExistPrev) {
                prevTime.style.border = "1px solid black";
            }
            isExistPrev = true;
            prevTime = timeElement;
            let time = timeElement.querySelector("p").textContent;
            timeElement.style.border = "1px solid orange";

            document.getElementById("tableNumber").style.display = 'block';
            clickTime(time);
        });
    }
}

let isRequestInProgress = false;

// 시간 클릭
let isVaildTable = true;
function clickTime(time) {
    if (isRequestInProgress) return;  // 요청 중복 방지
    isRequestInProgress = true;

    let storeId = document.getElementById("storeId").value;
    let date = document.getElementById("date").value;
    if (date === null || date === "") {
        document.getElementById("tableNumber").style.display = 'none';
        alert('날짜를 선택해주세요.');
        isRequestInProgress = false;
        return;
    }
    document.getElementById("table").value = "";

    if (isExistPrevTable) {
        prevTable.style.border = "1px solid black";
    }

    $.ajax({
        url: "/reservation/table-information",
        type: "post",
        data: { storeId: storeId, date: date, time: time },
        success: function (tableMap) {
            document.getElementById("time").value = time;
            for (let i = 0; i < 4; i++) {

                const div = document.getElementById('table' + i);
                const p = document.getElementById('pTable' + i);
                const pValue = p;
                if (i === 0) {
                    p.innerHTML = "<strong>1인 테이블</strong>" + "<br>" + "<br>" + "남은 테이블: " + tableMap.oneTable;
                    pValue.value = tableMap.oneTable;

                }

                else if (i === 1) {
                    p.innerHTML = "<strong>2인 테이블</strong>" + "<br>" + "<br>" + "남은 테이블: " + tableMap.twoTable;
                    pValue.value = tableMap.twoTable;
                }

                else if (i === 2) {
                    p.innerHTML = "<strong>4인 테이블</strong>" + "<br>" + "<br>" + "남은 테이블: " + tableMap.fourTable;
                    pValue.value = tableMap.fourTable;
                }
                else if (i === 3) {
                    p.innerHTML = "<strong>단체 테이블</strong>" + "<br>" + "<br>" + "남은 테이블: " + tableMap.partyTable;
                    pValue.value = tableMap.partyTable;
                }
                document.getElementById('table' + i).style.pointerEvents = 'auto';
                document.getElementById('table' + i).style.cursor = 'pointer';
                document.getElementById('table' + i).style.opacity = '1';
                if (pValue.value === 0) {
                    document.getElementById('table' + i).style.cursor = 'default';
                    document.getElementById('table' + i).style.opacity = '0.5';
                    document.getElementById('table' + i).style.pointerEvents = 'none';
                }

                


                p.style.textAlign = "center";
                p.style.fontSize = '16px';
                p.style.marginLeft = '13px';
                p.style.marginTop = '10px';
                div.appendChild(p);
            }

        },
        error: function () {
            alert("서버 오류가 발생했습니다.");
        },
        complete: function () {
            isRequestInProgress = false;  // 요청 완료 후 플래그 리셋
        }
    });
}
let isExistPrevTable = false;
let prevTable;
const table = document.getElementsByClassName("table");
for (let i = 0; i < table.length; i++) {

    document.getElementById("table" + i).addEventListener('click', function () {


        document.getElementById("table").value = document.getElementById("table" + i).querySelector('input[type="hidden"]').value;
        if (isExistPrevTable) {
            prevTable.style.border = "1px solid black";
        }
        isExistPrevTable = true;
        prevTable = document.getElementById("table" + i);
        document.getElementById("table" + i).style.border = "1px solid orange";
    });
}
