fetch('/owner/owner-header.html')
.then(response => response.text())
.then(data => {
    document.getElementById('header').innerHTML = data;
});

// fetch('footer.html')
// .then(response => response.text())
// .then(data => {
//     document.getElementById('footer').innerHTML = data;
// });