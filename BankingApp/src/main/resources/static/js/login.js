let username = document.getElementById("username").value;
let passwordInput = document.getElementById("password").value;
let form = document.getElementById("contactForm");


form.addEventListener("submit",(e) => {
    preventDefault();
    userLogin();
});

async function userLogin() {
    try {
        let response = await fetch(`http://localhost:8080/login`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify ({
                username: username,
                password: passwordInput
            })
        })
        let data = await response.json();
        console.log(data);

    } catch(e) {
        console.error("Incorrect username/password" + e);
    }
}
