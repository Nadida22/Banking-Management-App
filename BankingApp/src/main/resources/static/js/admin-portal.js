import { makePostRequest } from './requesthandlers.js';
import { createToken } from './schemas.js';

const url = `http://localhost:8080`;
const activeUsername = sessionStorage.getItem("username");
const activeToken = sessionStorage.getItem("token");

if (!activeUsername || !activeToken) {
    console.error("Username or token is missing in sessionStorage");
    // Handle missing username or token here
}

const userToken = createToken({ 
    username: activeUsername ? activeUsername.trim() : '', 
    token: activeToken ? parseInt(activeToken.trim()) : 0 
});

let allUsersButton = document.getElementById("allUsers");
let allAccountsButton = document.getElementById("allAccounts");

allUsersButton.addEventListener("click", () => handleAllUsersClick());
allAccountsButton.addEventListener("click", () => handleAllAccountsClick());

async function handleAllUsersClick() {
    try {
        let userData = await getAllUsers(userToken);
        console.log(userData);
        // Call display function here if needed
    } catch (error) {
        console.error("Error fetching all users:", error);
    }
}

async function handleAllAccountsClick() {
    try {
        let accountData = await getAllAccounts(userToken);
        console.log(accountData);
        // Call display function here if needed
    } catch (error) {
        console.error("Error fetching all accounts:", error);
    }
}

async function getAllUsers(tokenData) {
    const usernameUrl = `${url}/user/all`;
    return makePostRequest(usernameUrl, tokenData);
}

async function getAllAccounts(tokenData) {
    const accountsUrl = `${url}/account/all`;
    return makePostRequest(accountsUrl, tokenData);
}