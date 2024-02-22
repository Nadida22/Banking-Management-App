let userSchema = {
    username: "",
    password: "",
    email: "",
    firstName: "",
    lastName: "",
    isAdmin: false // Assuming boolean value
};

let accountSchema = {
    accountType: "",
    accountNumber: "", // or use a number if appropriate
    balance: 0, // Assuming balance is a number
    userId: null
};

let transactionSchema = {
    type: "DEPOSIT", // Consider using an enum for type
    amount: 0,
    accountId: null
};

let loginSchema = {
    username: "",
    password: "" // Password should be a string
};

let tokenSchema = {
    token: null,
    data: null
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
