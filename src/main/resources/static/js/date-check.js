document.addEventListener('DOMContentLoaded', function () {
    const startDateInput = document.getElementById('start-date');
    const endDateInput = document.getElementById('end-date');
    const buyNowButton = document.getElementById('buy-now-button');
    const warning = document.getElementById('warning-message');

    function checkIfDatesFilled() {
        const startDate = startDateInput.value;
        const endDate = endDateInput.value;

        if (startDate && endDate) {
            buyNowButton.removeAttribute('disabled');
        } else {
            buyNowButton.setAttribute('disabled', 'disabled');
            warning.textContent = "Please enter the dates you wish to start and end your subscription!";
        }
    }

    function compareDate() {
        const startDate = startDateInput.value;
        const endDate = endDateInput.value;

        if (endDate < startDate) {
            buyNowButton.setAttribute('disabled', 'disabled');
            warning.textContent = "Remember to set the start date before the end date!";
        } else {
            buyNowButton.removeAttribute('disabled');
        }
    }

    function bringToNextPage() {
        const startDate = startDateInput.value;
        const endDate = endDateInput.value;

        if (startDate && endDate) {
            const nextPageURL = '/products/payment' +
                '?startDate=' + encodeURIComponent(startDate) +
                '&endDate=' + encodeURIComponent(endDate);
            window.location.href = nextPageURL;
        }
    }

    startDateInput.addEventListener('change', checkIfDatesFilled);
    endDateInput.addEventListener('change', checkIfDatesFilled);
    startDateInput.addEventListener('change', compareDate);
    endDateInput.addEventListener('change', compareDate);
    buyNowButton.addEventListener('click', bringToNextPage);

    buyNowButton.addEventListener('click', function (event) {
        if (buyNowButton.hasAttribute('disabled')) {
            event.preventDefault();
        }
    });

    compareDate();
    checkIfDatesFilled();
});