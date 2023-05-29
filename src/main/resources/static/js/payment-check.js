function validateEmail() {
    var emailInput = document.getElementById("email");
    var email = emailInput.value;

    var emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

    if (!emailRegex.test(email)) {
        alert("Please enter a valid email address.");
        emailInput.focus();
        return false;
    }
    return true;
}

function validateCVC() {
    var cvcInput = document.getElementById("cvc");
    var cvc = cvcInput.value;

    if (cvc.length !== 3 || isNaN(cvc)) {
        alert("Please enter a valid CVC code consisting of 3 digits.");
        cvcInput.focus();
        return false;
    }
    return true;
}

function validateForm() {
    return validateEmail() && validateCVC();
}