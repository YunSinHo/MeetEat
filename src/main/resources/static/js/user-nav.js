fetch('/user/user-header.html')
.then(response => response.text())
.then(data => {
    document.getElementById('header').innerHTML = data;
});

fetch('/user/user-footer.html')
.then(response => response.text())
.then(data => {
    document.getElementById('footer').innerHTML = data;
});