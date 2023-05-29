function checkStartDate() {
    var startDate = document.getElementById("start-date");

    if (startDate === "") {
        alert("Please enter the date you wish to start your subscription");
        return false;
    }
    return true;
}

function checkEndDate() {
    var endDate = document.getElementById("end-date");

    if (endDate === "") {

        alert("Please enter the date you wish to end your subscription");
        return false;
    }
    return true;
}

function validateDate() {
    var buyNowButton = document.getElementById("buy-now-button");

    if (checkStartDate() && checkEndDate()) {
        buyNowButton.hidden = false;
    }
}

function compareDate() {
    var startDate = document.getElementById("start-date").value;
    var endDate = document.getElementById("end-date").value;

    if (endDate < startDate) {
        throw new Error("Start date needs to be before end date!");
    }
}

var buyNowButton = document.getElementById("buy-now-button");
buyNowButton.addEventListener("click", function (event) {
    event.preventDefault();
    validateDate();
});