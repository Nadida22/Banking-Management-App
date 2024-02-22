import { loginSchema } from './schemas.js';

let form = document.getElementById("contactForm").addEventListener("submit", handleFormSubmit);


async function handleFormSubmit(e) {
    e.preventDefault();
    const formData = getFormData();
    console.log(formData);
    try {
        const response = await loginUser(formData);
        const responseData = await response.json();
        console.log(responseData)


        if(response.ok){
            console.log(responseData);
            sessionStorage.setItem("token", responseData.token);
            sessionStorage.setItem("username", responseData.username);
         



//         window.location.href = "/user-portal";
         }


        }
     catch (error) {
        console.error(error);
        displayMessage("Account Not Created. Please Try Again");
    }
}


function getFormData() {
    return {
        ...loginSchema,
        username: document.getElementById("username").value,
        password: document.getElementById("password").value
    };


}

async function loginUser(userData) {
    let loginUrl = `http://localhost:8080/user/login` ;
    try {
        let response = await fetch(loginUrl, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify (userData)
        })
        return response;

    } catch(e) {
        console.error("Incorrect username or password" + e);
    }
}


function displayMessage(message) {
    document.getElementById("account-created").innerHTML = message;
}
