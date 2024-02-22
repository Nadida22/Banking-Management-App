import { createToken, createUser} from './schemas.js';

const url = `http://localhost:8080`;
const activeToken = parseInt(sessionStorage.getItem("token"));
const activeUsername = sessionStorage.getItem("username");

(async () => {
    console.log(`Token: ${activeToken}`);
    console.log(`Username: ${activeUsername}`);

    try {
        let userResponse = await getUserData();
        console.log(userResponse);

        // Example usage of getBalance and getAccounts
        // Assuming you have a userId to use
        // let userId = 'someUserId';
        // let balanceResponse = await getBalance(userId);
        // let accountsResponse = await getAccounts(userId);
    } catch (error) {
        console.error('Error:', error);
    }
})();

async function getUserData() {
    // get User info
    console.log(activeToken);
    let userData = createToken(activeUsername, activeToken);
    const usernameUrl = `http://localhost:8080/username`;
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
    let userData = new tokenSchema(activeUsername, activeToken);
    const balanceUrl = `${url}/user/${userId}/balance`;
    const response = await fetch(balanceUrl, {
        method: 'POST',
//        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(userData)
    });
    if (!response.ok) {
        throw new Error('Network response was not ok');
    }
    return response.json();
}

async function getAccounts(userId) {
    // getAccounts
    let userData = new tokenSchema(activeUsername, activeToken);
    const accountsUrl = `${url}/user/${userId}/account`;
    const response = await fetch(accountsUrl, {
        method: 'POST',
//        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(userData)
    });
    if (!response.ok) {
        throw new Error('Network response was not ok');
    }
    return response.json();
}

// Removed the incomplete and unused code
