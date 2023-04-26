var lastScrollTop; // This Variable will store the top position

navbar = document.getElementById('navbar-placeholder'); // Get The NavBar

window.addEventListener('scroll',function(){
    //on every scroll this function will be called

    var scrollTop = window.pageYOffset || document.documentElement.scrollTop;
    //This line will get the location on scroll

    if(scrollTop > lastScrollTop && scrollTop > 3){ //if it will be greater than the previous
        navbar.style.top='-80px';
        //set the value to the negative of height of navbar
    }

    else{
        navbar.style.top='0';
    }

    lastScrollTop = scrollTop; //New Position Stored
});