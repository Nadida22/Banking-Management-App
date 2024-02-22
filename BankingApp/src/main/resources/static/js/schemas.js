let userSchema = {
    username: "",
    password: "",
    email: "",
    firstName: "",
    lastName: "",
    isAdmin: "" 
};

let accountSchema = {
    accountType: "",
    accountNumber: "", 
    balance: 0,
    userId: 0
};

let transactionSchema = {
    type: "DEPOSIT", 
    amount: 0,
    accountId: 0
};

let loginSchema = {
    username: "",
    password: "" 
};

let tokenSchema = {
    token: 1,
    username:"",
    data: ""
};


function createUser(data = {}) {
    return { ...userSchema, ...data };
}

function createAccount(data = {}) {
    return { ...accountSchema, ...data };
}

function createTransaction(data = {}) {
    return { ...transactionSchema, ...data };
}

function createLogin(data = {}) {
    return { ...loginSchema, ...data };
}

function createToken(data = {}) {
    return { ...tokenSchema, ...data };
}

export { createUser, createAccount, createTransaction, createLogin, createToken };
