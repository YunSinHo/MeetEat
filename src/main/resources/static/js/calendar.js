// 달력 생성 및 월 이동 기능
document.addEventListener('DOMContentLoaded', function () {
    const daysContainer = document.getElementById('days');
    const monthYear = document.getElementById('monthYear');
    const prevMonthButton = document.getElementById('prevMonth');
    const nextMonthButton = document.getElementById('nextMonth');

    let today = new Date();
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

        for (let i = 0; i < firstDayWeekday; i++) {
            const emptyCell = document.createElement('div');
            daysContainer.appendChild(emptyCell);
        }

        for (let day = 1; day <= lastDayOfMonth.getDate(); day++) {
            const dayCell = document.createElement('div');
            dayCell.classList.add('day');
            dayCell.textContent = day;

            const date = new Date(year, month, day);

            if (year === today.getFullYear() && month === today.getMonth() && day === today.getDate()) {
                dayCell.classList.add('today');
            }

            if (date >= today && date <= threeWeeksLater) {
                dayCell.classList.add('clickable');
                dayCell.addEventListener('click', function () {
                    const selectedDate = `${year}-${month + 1}-${day}`;
                    sendDateToServer(selectedDate);
                });
            } else {
                dayCell.classList.add('disabled');
            }

            daysContainer.appendChild(dayCell);
        }
    }

    // 서버로 날짜 전송 함수
    function sendDateToServer(date) {
        fetch('/reservation/choice-date', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: new URLSearchParams({ date: date })
        })
        .then(response => response.text())
        .then(result => {
            console.log('서버 응답:', result); // 성공 시 서버 응답을 출력
            alert(`날짜 ${date} 예약이 완료되었습니다.`);
        })
        .catch(error => {
            console.error('날짜 전송 중 오류:', error);
            alert("날짜 전송에 실패했습니다.");
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
