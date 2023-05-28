function check() {
    const input = document.getElementById('password-confirm');
    if ((document.getElementById('password-confirm').value != document.getElementById('password').value)) {
        input.setCustomValidity('pASSword NOT MaTChIng!');
    } else {
        // input is valid -- reset the error message
        input.setCustomValidity('');
    }
}

document.getElementById('signup-form').addEventListener('submit', function (event) {
    event.preventDefault(); // Prevent the form submission by default

    // Perform the password check
    check();

    // Check if the form is now valid
    if (this.checkValidity()) {
        // Get the form values
        const email = document.getElementById('email').value;
        const password = document.getElementById('password').value;
        const firstName = document.getElementById('firstname').value;
        const lastName = document.getElementById('lastname').value;
        const phoneNumber = document.getElementById('phoneNumber').value;

        // Create the user object
        const user = {
            firstName: firstName,
            lastName: lastName,
            email: email,
            password: password,
            phoneNumber: phoneNumber,
            age : 18
            // Add any additional fields you want to include
        };

        // Send a POST request to your server
        fetch('api/user/signup', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(user),
        })
            .then(function (response) {
                // Handle the response from the server
                if (response.ok) {
                    // User created successfully
                    console.log('User created successfully!');
                } else {
                    // Error creating user
                    console.log('Error creating user.');
                }
            })
            .catch(function (error) {
                console.log('Error:', error);
            });
    }
});