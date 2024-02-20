let emailInput = document.getElementById("email").value;
let passwordInput = document.getElementById("password").value;
let logInButton = document.getElementById("logInButton");

logInButton.addEventListener("click", () => {
    let userData = JSON.parse(localStorage.getItem('formData'));
    for(var i = 0; i < userData.length; i++) {
        if(userData[i].email == emailInput) {
            if(userData[i].password == passwordInput) {
                console.log("Login Successful");
            } else {
                console.log("Incorrect username/password");
            }
        } else {
            console.log("Incorrect usernaame/password");
        }
    }

 

})