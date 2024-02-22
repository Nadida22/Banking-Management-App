document.addEventListener('DOMContentLoaded', function() {
    // Get the select element for users
    const usersSelect = document.getElementById('users');

    // Add event listener for change event
    usersSelect.addEventListener('change', function() {
        const selectedOption = usersSelect.value;

        if (selectedOption === 'allUsers') {
            // Fetch all users from the backend
            fetch('http://localhost:8080/user', {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json'
                }
            })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Failed to fetch users');
                }
                return response.json();
            })
            .then(users => {
                // Handle the response data and display it
                const usersContainer = document.querySelector('.border.rounded.p-3');
                usersContainer.innerHTML = ''; // Clear previous content

                users.forEach(user => {
                    const userElement = document.createElement('div');
                    userElement.innerHTML = `
                        <p><strong>userId:</strong> ${user.userId} </p>
                        <p><strong>Role:</strong> ${user.role} </p>
                        <p><strong>Name:</strong> ${user.firstName} ${user.lastName}</p>
                        <p><strong>Email:</strong> ${user.email}</p>
                        <p><strong>Username:</strong> ${user.username}</p>
                        <hr>
                    `;
                    usersContainer.appendChild(userElement);
                });
            })
            .catch(error => {
                console.error('Error fetching users:', error.message);
            });
        }
    });
});
