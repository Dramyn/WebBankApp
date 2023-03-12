package com.bank.web.Service;

import com.bank.web.Models.Account;

import java.security.Principal;

public interface AccountService {

    Account createAccount();
    void deposit(String accountType, double amount, Principal principal);
    void withdraw(String accountType, double amount, Principal principal);


}
