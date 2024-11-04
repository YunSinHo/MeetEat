fetch('/user/user-header.html')
.then(response => response.text())
.then(data => {
    document.getElementById('header').innerHTML = data;
});

// fetch('/user/user-footer.html')
// .then(response => response.text())
// .then(data => {
//     document.getElementById('footer').innerHTML = data;g
// });
$.ajax({
    url: '/user-profile/main-image',
    type: 'POST',
    success: function (imagePath) {
        document.getElementById("mainImage").src = imagePath; 
    },
    error: function () {
        console.error("이미지 로드 실패");
    }
});