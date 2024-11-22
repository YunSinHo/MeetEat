// 달력 생성 및 월 이동 기능
document.addEventListener('DOMContentLoaded', function () {
    const daysContainer = document.getElementById('days');
    const monthYear = document.getElementById('monthYear');
    const prevMonthButton = document.getElementById('prevMonth');
    const nextMonthButton = document.getElementById('nextMonth');

    let today = new Date();
    today.setHours(0, 0, 0, 0);
    let currentYear = today.getFullYear();
    let currentMonth = today.getMonth();

    function renderCalendar(year, month) {
        daysContainer.innerHTML = ''; // 이전 날짜 지우기
        const threeWeeksLater = new Date(today);
        threeWeeksLater.setDate(today.getDate() + 21); // 3주 후
    
        const firstDayOfMonth = new Date(year, month, 1);
        const lastDayOfMonth = new Date(year, month + 1, 0);
    
        monthYear.textContent = `${year}년 ${month + 1}월`;
        const firstDayWeekday = firstDayOfMonth.getDay();
    
        // 첫 번째 주의 빈칸 생성
        for (let i = 0; i < firstDayWeekday; i++) {
            const emptyCell = document.createElement('div');
            daysContainer.appendChild(emptyCell);
        }
    
        // 날짜 셀 생성
        for (let day = 1; day <= lastDayOfMonth.getDate(); day++) {
            const dayCell = document.createElement('div');
            dayCell.classList.add('day');
            dayCell.textContent = day;
    
            const date = new Date(year, month, day);
    
            if (year === today.getFullYear() && month === today.getMonth() && day === today.getDate()) {
                dayCell.classList.add('today');
            }
    
            // 클릭 가능 날짜에만 클릭 이벤트 추가
            if (date >= today && date <= threeWeeksLater) {
                dayCell.classList.add('clickable');
                dayCell.addEventListener('click', function () {
                    // 기존 선택된 날짜의 'selected' 클래스 제거
                    const previouslySelected = document.querySelector('.day.selected');
                    if (previouslySelected) {
                        previouslySelected.classList.remove('selected');
                    }
    
                    // 클릭한 날짜에 'selected' 클래스 추가하여 빨간색으로 고정
                    dayCell.classList.add('selected');
                    
                    const selectedDate = `${year}-${month + 1}-${day}`;
                    sendDateToServer(selectedDate);
                });
            } else {
                dayCell.classList.add('disabled');
            }
    
            daysContainer.appendChild(dayCell);
        }
    }
    

    // 서버로 날짜 전송 함수(3주 까지)
    function sendDateToServer(date, storeId) {
        storeId = document.getElementById("storeId").value;
        $.ajax({
            url : "/reservation/choice-date",
            type : "POST",
            data : {date : date, storeId : storeId},
            success : function(date) {
                document.getElementById("date").value = date;
            },
            error : function () {
                alert("서버 오류가 발생했습니다.")
            }
        });
    }

    // 이전 달로 이동
    prevMonthButton.addEventListener('click', function () {
        if (currentMonth === 0) {
            currentMonth = 11;
            currentYear -= 1;
        } else {
            currentMonth -= 1;
        }
        renderCalendar(currentYear, currentMonth);
    });

    // 다음 달로 이동
    nextMonthButton.addEventListener('click', function () {
        if (currentMonth === 11) {
            currentMonth = 0;
            currentYear += 1;
        } else {
            currentMonth += 1;
        }
        renderCalendar(currentYear, currentMonth);
    });

    renderCalendar(currentYear, currentMonth);
});
