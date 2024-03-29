import { createLogin } from './schemas.js';

let ADMINTOKEN = 1000055002

async function handleFormSubmit(e) {
    e.preventDefault();
    const formData = getFormData();

    try {
        let redirectLink;
        const response = await loginUser(formData);

        if (response.ok) {
            const responseData = await response.json();

            // Store token and username in sessionStorage.
            sessionStorage.setItem("token", responseData.token);
            sessionStorage.setItem("username", responseData.username);

            let token = parseInt(responseData.token.trim());

            console.log(token);
            if (token === ADMINTOKEN){
                redirectLink = "/admin-portal";
            } else {
                redirectLink = "/user-portal";
            }




            // Logic to redirect based on user role

             window.location.href = redirectLink;

        } else {
            // Handle HTTP errors
            throw new Error('Failed to log in');
        }
    } catch (error) {
        console.error(error);
        displayMessage("Username or password is invalid. Please try again.");
    }
}

function getFormData() {
    return createLogin({
        username: document.getElementById("username").value,
        password: document.getElementById("password").value
    });
}

async function loginUser(userData) {
    let loginUrl = `http://localhost:8080/user/login`;
    try {
        let response = await fetch(loginUrl, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(userData)
        });
        return response;
    } catch (e) {
        console.error("Login error: " + e.message);
        throw e; // Re-throw the error to be caught by the caller
    }
}

function displayMessage(message) {
    document.getElementById("signin-message").textContent = message;
}

let submitButton = document.getElementById("submitButton")
submitButton.addEventListener("click", handleFormSubmit);