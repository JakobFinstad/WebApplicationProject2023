function check() {
    const input = document.getElementById('password-confirm');
    if ((document.getElementById('password-confirm').value != document.getElementById('password').value)) {
        input.setCustomValidity('password not matching!');
    } else {
        // input is valid -- reset the error message
        input.setCustomValidity('');
    }
}

document.getElementById("submit").addEventListener("submit", check);