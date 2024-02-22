import { userSchema } from './schemas.js';

document.getElementById("contactForm").addEventListener("submit", handleFormSubmit);

async function handleFormSubmit(e) {
    e.preventDefault();
    const formData = getFormData();
    console.log(formData);
    try {
        const response = await registerUser(formData);
        displayMessage("Account Created!");
        if(response.ok){
        window.location.href = "/login.html";

        }
    } catch (error) {
        console.error(error);
        displayMessage("Account Not Created. Please Try Again");
    }
}

function getFormData() {
    return {
        ...userSchema,
        username: document.getElementById("username").value,
        password: document.getElementById("password").value,
        email: document.getElementById("email").value,
        firstName: document.getElementById("firstname").value,
        lastName: document.getElementById("lastname").value,
        isAdmin: document.getElementById("adminUser").value
    };
}

async function registerUser(userData) {
    const response = await fetch(`http://localhost:8080/user`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(userData)
    });

    if (!response.ok) {
        throw new Error('Network response was not ok');
    }

    return response.json();
}

function displayMessage(message) {
    document.getElementById("signin-message").innerHTML = message;
}