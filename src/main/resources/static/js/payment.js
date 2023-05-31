function displayInfo() {
    const params = new URLSearchParams(window.location.search);
    const productId = params.get('prodId');
    const startDate = params.get('startDate');
    const endDate = params.get('endDate');

    document.getElementById('product-name').textContent = "Product name: " + productId;
    document.getElementById("userId").setAttribute("value", productId);

    document.getElementById('start-date').textContent = startDate;
    document.getElementById('start-date').setAttribute("value",startDate)

    document.getElementById('end-date').textContent = endDate;
    document.getElementById('end-date').setAttribute("value",endDate)
    sessionStorage.setItem('userId', productId);
}


displayInfo();

document.getElementById('sub-form').addEventListener('submit', function (event) {
    event.preventDefault();



    // Get the form data
    const userId = document.getElementById("userId").title;
    const productId = document.getElementById("productId").title;
    const startDate = document.getElementById("start-date").textContent;
    const endDate = document.getElementById("end-date").textContent;




    if (this.checkValidity()) {
        const subscription = {
            startDate: startDate,
            endDate: endDate,
            productId: productId,
            userId: userId,
        };

        fetch("/api/subscriptions/place-order", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(subscription),
        })
            .then(function (response) {
                if (response.ok) {
                    document.getElementById("info-box").textContent = "You subscribed successfully!"
                    window.location.href = "https://group06.web-tek.ninja:8080/"
                    console.log("Subscription added successfully!");
                } else {
                    console.log('Error adding subscription')
                }
            })
            .catch(function (error) {
                console.error(error);
            });
    }
});
