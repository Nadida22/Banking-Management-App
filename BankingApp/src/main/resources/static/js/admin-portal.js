import { makePostRequest, makeDeleteRequest } from './requesthandlers.js';
import { createToken } from './schemas.js';

const url = `http://localhost:8080`;
const activeUsername = sessionStorage.getItem("username");
const activeToken = sessionStorage.getItem("token");

if (!activeUsername || !activeToken) {
    console.error("Username or token is missing in sessionStorage");
    // Handle missing username or token
}

const userToken = createToken({ 
    username: activeUsername ? activeUsername.trim() : '', 
    token: activeToken ? parseInt(activeToken.trim()) : 0 
});

let allUsersButton = document.getElementById("allUsers");
let allAccountsButton = document.getElementById("allAccounts");
let contentContainer = document.getElementById("user-container");

allUsersButton.addEventListener("click", () => handleAllUsersClick());
allAccountsButton.addEventListener("click", () => handleAllAccountsClick());


async function displayAllAccounts(accountData) {
    let accounts = accountData.flat(Infinity);
    contentContainer.innerHTML = ''; // Clear previous content

    accounts.forEach(account => {
        const accountCard = document.createElement('div');
        accountCard.className = 'card mb-3';

        const cardBody = document.createElement('div');
        cardBody.className = 'card-body';

        const accountInfo = document.createElement('h5');
        accountInfo.className = 'card-title';
        const formattedAccountNumber = account.accountNumber.toString().slice(-4);
        accountInfo.textContent = `Account ID: ${account.accountId}, User ID: ${account.userId}`;

        const accountDetails = document.createElement('p');
        accountDetails.className = 'card-text';
        accountDetails.textContent = `Account Number: XXXX XXXX XXXX ${formattedAccountNumber}, Balance: $${account.balance}`;

        cardBody.appendChild(accountInfo);
        cardBody.appendChild(accountDetails);
        accountCard.appendChild(cardBody);
        contentContainer.appendChild(accountCard);

        const deleteButton = document.createElement('button');
        deleteButton.className = 'btn btn-danger btn-sm';
        deleteButton.textContent = 'Delete';
        deleteButton.onclick = async () => {
            try {
                // type checking 
                console.log(typeof account.accountId);
                console.log(account.accountId);
                await deleteAccount(account.accountId);
                displayAllAccounts(await getAllAccounts(userToken)); // Refresh accounts
            } catch (error) {
                console.error("Error deleting account:", error);
            }
        };

        cardBody.appendChild(deleteButton);


    });
}


async function displayAllUsers(userData) {
    let users = userData.flat(Infinity);
    contentContainer.innerHTML = ''; // Clear previous content

    users.forEach(user => {
        const userCard = document.createElement('div');
        userCard.className = 'card mb-3 shadow-sm'; // shadow?

        const cardBody = document.createElement('div');
        cardBody.className = 'card-body';

        // User Information
        const userInfo = document.createElement('h5');
        userInfo.className = 'card-title';
        const fullName = `${user.firstName} ${user.lastName}`;
        userInfo.textContent = `Name: ${fullName}, User ID: ${user.userId}`;

        // User Details
        const userDetails = document.createElement('p');
        userDetails.className = 'card-text';
        const numberOfAccounts = user.accounts ? user.accounts.length : 'No accounts';
        userDetails.textContent = `Email: ${user.email}, Accounts: ${numberOfAccounts}`;

     
        if (user.additionalDetails) {
            const additionalDetails = document.createElement('p');
            additionalDetails.className = 'card-text text-muted'; 
            additionalDetails.textContent = user.additionalDetails; 
            cardBody.appendChild(additionalDetails);
        }

        cardBody.appendChild(userInfo);
        cardBody.appendChild(userDetails);
        userCard.appendChild(cardBody);
        contentContainer.appendChild(userCard);

            // Create Delete Button
            const deleteButton = document.createElement('button');
            deleteButton.className = 'btn btn-danger btn-sm';
            deleteButton.textContent = 'Delete';
            deleteButton.onclick = async () => {
                try {
                    console.log(typeof user.userId);
                    await deleteUser(user.userId);
                    displayAllUsers(await getAllUsers(userToken)); // Refresh users
                } catch (error) {
                    console.error("Error deleting user:", error);
                }
            };
    
            cardBody.appendChild(deleteButton);

    });
}




// Not working 
async function deleteAccount(accountId) {
    try {
        const deleteUrl = `${url}/account/${accountId}`;
        console.log(userToken);
        const response = await makeDeleteRequest(deleteUrl, userToken);
        if (!response.ok) {
            throw new Error(`HTTP status ${response.status}`);
        }
        return response.json();
    } catch (error) {
        console.error("Error deleting account:", error);
    }
    }






async function deleteUser(userId) {
    const deleteUrl = `${url}/user/${userId}`;
    return makeDeleteRequest(deleteUrl, userToken);
}


async function handleAllUsersClick() {
    try {
        let userData = await getAllUsers(userToken);
        // console.log(userData);
        // call display
        displayAllUsers(userData)

    } catch (error) {
        console.error("Error fetching all users:", error);
    }
}

async function handleAllAccountsClick() {
    try {
        let accountData = await getAllAccounts(userToken);
        // console.log(accountData);
        // Call display
        displayAllAccounts(accountData)
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