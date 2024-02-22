import { createToken, createUser} from './schemas.js';

const url = `http://localhost:8080`;
const rawToken = sessionStorage.getItem("token").trim();
const activeUsername = sessionStorage.getItem("username");
let activeToken = parseInt(rawToken);


(async () => {
    displayData();
  })();



async function displayData(){
    let userData = await getUserData();
    let balanceData = await getBalance();
    let userId = parseInt(userData.userId.trim());

    let username = userData.username;
 

    let balance = balanceData;
    console.log(balance);

   
}




async function getUserData() {
    // get User info
    let userData = createToken({ username: activeUsername, token: activeToken });
    const usernameUrl = `${url}/username`;
    const response = await fetch(usernameUrl, {
        method: 'POST',
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(userData)
    });
    if (!response.ok) {
        throw new Error('Network response was not ok');
    }
    return response.json();
}

async function getBalance(userId) {
    // getBalance
    let userData = createToken({ username: activeUsername, token: activeToken });
    const balanceUrl = `${url}/user/${userId}/balance`;
    const response = await fetch(balanceUrl, {
        method: 'POST',
       headers: { "Content-Type": "application/json" },
        body: JSON.stringify(userData)
    });
    if (!response.ok) {
        throw new Error('Network response was not ok');
    }
    return response.json();
}

async function getAccounts(userId) {
    // getAccounts
    let userData = createToken({ username: activeUsername, token: activeToken });
    const accountsUrl = `${url}/user/${userId}/account`;
    const response = await fetch(accountsUrl, {
        method: 'POST',
       headers: { "Content-Type": "application/json" },
        body: JSON.stringify(userData)
    });
    if (!response.ok) {
        throw new Error('Network response was not ok');
    }
    return response.json();
}

