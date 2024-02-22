let getAccounts = document.getElementById("accountInfo");
console.log(getAccounts);
let userAccounts = [];

function populateUserAccounts(accounts) {
    getAccounts.innerHTML = "";
    for(acc of accounts) {
        let newDiv = document.createElement('div');
        newDiv.innerHTML = `
        <div class="card-body p-0">
            <h2 class="fw-bolder">${acc.accountType}</h2>
            <p>${acc.accountId}</p>
            <label >${acc.balance}</label>
            <p>${acc.accountNumber}</p>
            </div>
        <div class="card-body p-0"> 
        <input type="number" id="inputamount" class="form-control" placeholder="Please input how much money you would like to withdraw/deposit">
        </div>
        <button type="submit" class="btn btn-primary btn-lg" id="withdraw">Withdraw</button>
        <button type="submit" class="btn btn-primary btn-lg" id="deposit">Deposit</button>
        <br>
        <br>
        `;
        getAccounts.append(newDiv);
        let withdraw = document.getElementById("withdraw");
        let deposit = document.getElementById("deposit");
        let inputAmount = document.getElementById("inputamount");
    }
}

(async () => {
    let response = await fetch(`http://localhost:8080/user/1/account`);
    let data = await response.json();
    console.log(data);
    userAccounts = data;
    populateUserAccounts(userAccounts);
})();