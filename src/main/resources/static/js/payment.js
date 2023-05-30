document.addEventListener('DOMContentLoaded', function () {

    function displayInfo() {
        const params = new URLSearchParams(window.location.search);
        const productId = params.get('prodId');
        const startDate = params.get('startDate');
        const endDate = params.get('endDate');

        document.getElementById('product-name').textContent = "Product name: " + productId;
        document.getElementById('start-date').textContent = "Start date: " + startDate;
        document.getElementById('end-date').textContent = "End date: " + endDate;
    }

    displayInfo();
});