let userName = document.getElementById("name");
let userEmail = document.getElementById("email");
let userPassword = document.getElementById("password");
let button = document.getElementById("submitButton");
let form = document.getElementById("contactForm")


button.addEventListener("click", registerUser);

function registerUser(e) {
    let formData = JSON.parse(localStorage.getItem("formData")) || [];
    let user = {
        name: userName.value,
        email: userEmail.value,
        password: userPassword.value
    };
        formData.push(user);
        localStorage.setItem("formData", JSON.stringify(formData));
        console.log("Account Created!")
        let p  = document.createElement("p");
        let nextStep = document.getElementById("submitSuccessMessage");
        nextStep.appendChild(p);
        nextStep.innerHTML = "Account created! Please log in " 
    e.preventDefault();
}