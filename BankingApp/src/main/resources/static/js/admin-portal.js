import { makePostRequest, makePatchRequest, makeDeleteRequest } from './requesthandlers.js';
import { createToken } from './schemas.js';



const url = `http://localhost:8080`;
const activeUsername = sessionStorage.getItem("username").trim();
const activeToken = parseInt(sessionStorage.getItem("token").trim());

const userToken = createToken({username: activeUsername, token: activeToken});


let allUsersButton = document.getElementById("allUsers");
let allAccountsButton = document.getElementById("allAccounts");
allUsersButton.addEventListener("click", getAllUsers);
allAccountsButton.addEventListener("click", getAllAccounts);


async function displayAccounts(){
    let accountData = await getAllAccountData(userToken);
    console.log(accountData);



}
async function displayUsers(){
    let userData = await getAllUserData(userToken);
    console.log(userData)

}






async function getAllUsers(tokenData) {
    const usernameUrl = `${url}/user/all`;
    return makePostRequest(usernameUrl, tokenData);
}


async function getAllAccounts(tokenData) {
    const accountsUrl = `${url}/account/all`;
    return makePostRequest(accountsUrl, tokenData);
}



