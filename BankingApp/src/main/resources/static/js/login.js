
let form = document.getElementById("contactForm");


form.addEventListener("submit",(e) => {
    e.preventDefault();
    userLogin();
});




async function userLogin() {
    let usernameInput = document.getElementById("username").value;
    let passwordInput = document.getElementById("password").value;
    let loginUrl = `http://localhost:8080/user/login` ;
    try {
        let response = await fetch(loginUrl, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify ({
                username: usernameInput,
                password: passwordInput
            })
        })
        let data = await response.json();
        console.log(response.status);
//        window.location.href = './useraccount.html'

    } catch(e) {
        console.error("Incorrect username/password" + e);
    }
}

