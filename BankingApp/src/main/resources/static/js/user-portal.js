import { makePostRequest, makePatchRequest, makeDeleteRequest } from './requesthandlers.js';
import { createToken } from './schemas.js';


const url = `http://localhost:8080`;
const activeUsername = sessionStorage.getItem("username").trim();
const activeToken = parseInt(sessionStorage.getItem("token").trim());

// Centralize the token creation
const userToken = createToken({ username: activeUsername, token: activeToken });

(async () => {
    try {
        let userData = await getUserData(userToken);
        let rawUserId = userData.userId;
        let userId = parseInt(rawUserId);
        let balanceData = await getBalance(userId, userToken);
        let accountData = await getAccounts(userId, userToken);



        let firstName = userData.firstName;
        let balance = balanceData.balance;
        let rawAccounts = accountData.accounts;
        let accounts = accountData.flat(Infinity);
 


        let balanceDisplay = document.getElementById("account-balance");
        balanceDisplay.innerHTML = `$ ${balance}`;
        console.log(accounts);
        if (accounts === undefined) {
            // handle undefined accounts
        } else {
            console.log(accounts[0]);
            let accountsContainer = document.getElementById('content-container'); // Get the existing container
        
            for (let i = 0; i < accounts.length; i++) {
                const accountDiv = document.createElement('div');
                accountDiv.className = 'account-item mb-3 d-flex justify-content-between align-items-center';
        
                const balanceInfo = document.createElement('p');
                const formattedAccountNumber = accounts[i].accountNumber.toString().slice(-4);
                balanceInfo.textContent = `$XXXX XXXX XXXX ${formattedAccountNumber} - ${accounts[i].accountType} - $${accounts[i].balance}`;
        
                const transactionsButton = document.createElement('button');
                transactionsButton.className = 'btn btn-primary btn-sm';
                transactionsButton.textContent = 'View Transactions';
                transactionsButton.onclick = () => {
                    window.location.href = `/transactions/${accounts[i].id}`; 
                };
        
                accountDiv.appendChild(balanceInfo);
                accountDiv.appendChild(transactionsButton);
                
                accountsContainer.appendChild(accountDiv); 
            }
        }
            
            

         }
        
    catch (error) {
        console.error('Error:', error);
    }
})();


async function getUserData(tokenData) {
    const usernameUrl = `${url}/username`;
    return makePostRequest(usernameUrl, tokenData);
}

async function getBalance(userId, tokenData) {
    const balanceUrl = `${url}/user/${userId}/balance`;
    return makePostRequest(balanceUrl, tokenData);
}

async function getAccounts(userId, tokenData) {
    const accountsUrl = `${url}/user/${userId}/account`;
    return makePostRequest(accountsUrl, tokenData);
}
