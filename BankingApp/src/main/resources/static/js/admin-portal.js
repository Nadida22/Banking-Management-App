
    let allUsers = document.getElementById('allUsers')
    let userContainer = document.getElementById("userContainer")


    // Add event listener for change event
    allUsers.addEventListener('click', function() {

            const token = 1000055002;

            const requestBody = {
                    token: token
                };

            fetch('http://localhost:8080/user/all', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                }
                body: JSON.stringify(requestBody)
            })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Failed to fetch users');
                }
                return response.json();
            })
            .then(users => {
                // Handle the response data and display it
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
    });
