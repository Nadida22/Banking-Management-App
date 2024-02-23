import { makePostRequest, makeDeleteRequest } from './requesthandlers.js';
import { createToken } from './schemas.js';

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
const url = "http://localhost:8080/user";
return makePostRequest(url, userData);

}

function displayMessage(message) {
    document.getElementById("signin-message").innerHTML = message;
}