 const swiper = new Swiper(".mySwiper", {
    loop: true,
    autoplay: {
    delay: 6000,
},
    speed: 3000,
    slidesPerView: "auto",
    centeredSlides: true,
    spaceBetween: 20,
    pagination: {
    el: ".swiper-pagination",
    type: "fraction",
},
    navigation: {
    nextEl: ".swiper-button-next",
    prevEl: ".swiper-button-prev",
},
});

th:href="@{https://cdn.jsdelivr.net/npm/swiper9/swiper-bundle.min.js}"