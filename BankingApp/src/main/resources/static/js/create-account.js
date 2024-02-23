import { makePostRequest, makePatchRequest, makeDeleteRequest } from './requesthandlers.js';
import { createToken, createAccount } from './schemas.js';


const url = `http://localhost:8080`;
const activeUsername = sessionStorage.getItem("username").trim();
const activeToken = parseInt(sessionStorage.getItem("token").trim());


const userToken = createToken({ 
    username: activeUsername, 
    token: activeToken 
});


(async () => {

    populateDropdown();


})();



async function populateDropdown() {
    let userData = await getAllUsers(userToken);
    let users = userData.flat(Infinity);
    
    let selectElement = document.getElementById('userId');
    users.forEach(user => {
        let userEntry = `${user.firstName} ${user.lastName}`;
        const option = document.createElement('option');
        option.value = user.userId;
        option.textContent = userEntry;
        selectElement.appendChild(option);
    });
}


document.getElementById('accountForm').addEventListener('submit', async (e) => {
    e.preventDefault();


    
let accountType = document.getElementById(`accountType`).value.toUpperCase();
let accountBalance = document.getElementById(`balance`).value;
let userId = document.getElementById(`userId`).value
let accountData = createAccount({accountType: accountType, userId: userId, balance: accountBalance});
let tokenAccountData = createToken({username: activeUsername, token: activeToken, data: accountData})


let response = await registerAccount(tokenAccountData);
console.log(response);
});

async function registerAccount(tokenAccountData) {
    const registerUrl = `${url}/account`; 
    let response = await makePostRequest(registerUrl, tokenAccountData);
    return response; 
}

async function getAllUsers(tokenData) {
    const usernameUrl = `${url}/user/all`;
    return makePostRequest(usernameUrl, tokenData);
}
