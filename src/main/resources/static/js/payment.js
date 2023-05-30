    function displayInfo() {
        const params = new URLSearchParams(window.location.search);
        const productId = params.get('prodId');
        const startDate = params.get('startDate');
        const endDate = params.get('endDate');

        document.getElementById('product-name').textContent = "Product name: " + productId;
        document.getElementById('start-date').textContent = "Start date: " + startDate;
        document.getElementById("userId").setAttribute("value", productId);

        document.getElementById('end-date').textContent = "End date: " + endDate;
    }


    displayInfo();

    document.getElementById('sub-form').addEventListener('submit', function (event) {
        event.preventDefault();


        // Get the form data
        const userId = document.getElementById("userId").value;
        const productName = document.getElementById("productName").value;
        const startDate = document.getElementById("startDate").value;
        const endDate = document.getElementById("endDate").value;


        if (this.checkValidity()) {
            const subscription = {
                userId: userId,
                productName: productName,
                startDate: startDate,
                endDate: endDate
            };

            fetch("/api/subscriptions/place-order", {
                method: "POST",
                body: JSON.stringify(subscription),
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(subscription),
            })
                .then(function (response) {
                    if (response.ok) {
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
