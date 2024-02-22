
let form = document.getElementById("contactForm");


form.addEventListener("submit",(e) => {
    e.preventDefault();
    userLogin();
});

async function userLogin() {
    let username = document.getElementById("username").value;
    let passwordInput = document.getElementById("password").value;
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
        console.log(response.status);
        window.location.href = './useraccount.html'

    } catch(e) {
        console.error("Incorrect username/password" + e);
    }
}
