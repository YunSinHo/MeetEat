

let currentIndex = 0;
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
