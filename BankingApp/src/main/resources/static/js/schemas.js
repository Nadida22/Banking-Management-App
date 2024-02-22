

let userSchema = {
    username: "",
    password: "",
    email: "",
    firstName: "",
    lastName: "",
    isAdmin: "USER"

}

let accountSchema = {
    accountType: "",
    accountNumber: 0,
    balance: null,
    userId: null

}

let transactionSchema = {
    type: "DEPOSIT",
    amount: 0,
    accountId: null

}


let loginSchema = {
    username: "DEPOSIT",
    password: 0,
    data: null

}

export { userSchema, accountSchema, transactionSchema, loginSchema};