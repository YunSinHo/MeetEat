function oninputPhone(target) {
    target.value = target.value
        .replace(/[^0-9]/g, '')
        .replace(/(^02.{0}|^01.{1}|[0-9]{3,4})([0-9]{3,4})([0-9]{4})/g, "$1-$2-$3");
}

function selectGender(gender) {
    // 모든 박스에서 selected 클래스 제거
    document.getElementById('maleBox').classList.remove('selected');
    document.getElementById('femaleBox').classList.remove('selected');

    // 클릭된 박스에만 selected 클래스 추가
    if (gender === 'M') {
        document.getElementById('maleBox').classList.add('selected');
        document.getElementById('gender').value = gender;
    } else if (gender === 'F') {
        document.getElementById('femaleBox').classList.add('selected');
        document.getElementById('gender').value = gender;
    }
}

function populateYears() {
    const yearSelect = document.getElementById('yearSelect'); // 연도 선택 박스
    for (let i = 2024; i >= 1900; i--) { // 2024년부터 1900년까지
        const option = document.createElement('option');
        option.value = i;
        option.textContent = i;
        yearSelect.appendChild(option);
    }
}

function populateMonths() {
    const monthSelect = document.getElementById('monthSelect'); // 월 선택 박스
    for (let i = 1; i <= 12; i++) {
        const option = document.createElement('option'); // 새 옵션 요소 생성
        option.value = i; // 값 설정
        option.textContent = i; // 표시될 텍스트 설정
        monthSelect.appendChild(option); // 선택 박스에 옵션 추가
    }
}

function populateDays() {
    const daySelect = document.getElementById('daySelect'); // 일 선택 박스
    daySelect.innerHTML = '<option value="">일</option>'; // 초기화
    const monthValue = document.getElementById('monthSelect').value; // 선택된 월
    const yearValue = document.getElementById('yearSelect').value; // 선택된 연도

    if (monthValue && yearValue) {
        const daysInMonth = new Date(yearValue, monthValue, 0).getDate(); // 해당 월의 일 수
        for (let i = 1; i <= daysInMonth; i++) {
            const option = document.createElement('option');
            option.value = i;
            option.textContent = i;
            daySelect.appendChild(option);
        }
    }
}

// 월 또는 연도가 변경될 때 일을 업데이트
document.getElementById('yearSelect').addEventListener('change', populateDays);
document.getElementById('monthSelect').addEventListener('change', populateDays);

// 페이지가 로드될 때 년, 월, 일을 추가합니다.
window.onload = function() {
    populateYears();
    populateMonths();
}

function changePage(page) {
    // 페이지 이동 로직을 추가할 수 있습니다
    alert("페이지 " + page + "로 이동");
}

function goNextPage() {
    window.location.href = "nextpage.html";  // 다음 페이지로 이동
}