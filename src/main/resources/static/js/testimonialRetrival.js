// Make an HTTP request to retrieve testimonials and user information
fetch('api/testimonial')
    .then(response => response.json())
    .then(data => {
        // Assuming the data is an array of testimonials
        const firstThreeTestimonials = data.slice(0, 3); // Retrieve only the first three testimonials

        firstThreeTestimonials.forEach((testimonial, index) => {
            // Retrieve the necessary information from the testimonial object
            const { statement, testimonialId, userImageSrc, userName } = testimonial;

            // Update the corresponding HTML elements with the retrieved information
            const reviewParagraphs = document.querySelectorAll('.review-paragraph');
            const reviewImages = document.querySelectorAll('.review-image');
            const reviewNames = document.querySelectorAll('.review-name');

            // Update the text content and image source of the elements at the corresponding index
            reviewParagraphs[index].textContent = statement;
            reviewImages[index].src = userImageSrc;
            reviewNames[index].textContent = userName;
        });
    })
    .catch(error => {
        // Handle any errors that occur during the fetch request
        console.error('Error:', error);
    });
