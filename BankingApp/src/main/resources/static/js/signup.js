
let form = document.getElementById("contactForm");


form.addEventListener("submit", (e) =>{
    preventDefault();
    registerUser();
});

async function registerUser() {
    try {
    let firstName = document.getElementById("firstname").value;
    let lastName = document.getElementById("lastname").value;
    let userName = document.getElementById("username").value;
    let userEmail = document.getElementById("email").value;
    let userPassword = document.getElementById("password").value;
    let isAdmin = document.getElementById("adminUser").value;

    let text = document.getElementById("account-created");







        let response = await fetch(`http://localhost:8080/user`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                role: isAdmin,
                firstName: firstName,
                lastName: lastName,
                username: userName,
                email: userEmail,
                password: userPassword
            })
        })
        let data = await response.json();
        console.log(data);
        text.innerHTML = "Account Created!"
    } catch(e) {
        console.error(e);
        text.innerHTML = "Please try again."
    }
}

