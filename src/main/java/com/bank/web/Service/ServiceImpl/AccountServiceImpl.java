package com.bank.web.Service.ServiceImpl;

import com.bank.web.Models.Account;
import com.bank.web.Models.Client;
import com.bank.web.Models.Transaction;
import com.bank.web.Repositories.AccountRepository;
import com.bank.web.Service.AccountService;
import com.bank.web.Service.ClientService;
import com.bank.web.Service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.Date;

@Service
public class AccountServiceImpl implements AccountService {
    private static int nextAccountNumber = 10001;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientService clientService;

    @Autowired
    private TransactionService transactionService;

    public Account createAccount() {
        Account account = new Account();
        account.setBalance(new BigDecimal(0.0));
        account.setAccountNumber(accountGen());

        accountRepository.save(account);

        return accountRepository.findByAccountNumber(String.valueOf(account.getAccountNumber()));
    }

    public void deposit(String accountType, double amount, Principal principal) {
        Client client = clientService.findByNif(principal.getName());

            Account account = client.getAccount();
            account.setBalance(account.getBalance().add(new BigDecimal(amount)));
            accountRepository.save(account);

            Date date = new Date();

            Transaction transaction = new Transaction(date, "Deposit", "Finished", amount, account.getBalance(), account);
            transactionService.saveDepositTransaction(transaction);


    }

    public void withdraw(String accountType, double amount, Principal principal) {
        Client client = clientService.findByNif(principal.getName());

        Account account = client.getAccount();
        account.setBalance(account.getBalance().subtract(new BigDecimal(amount)));
        accountRepository.save(account);

        Date date = new Date();

        Transaction transaction = new Transaction(date, "Withdrawal", "Finished", amount, account.getBalance(), account);
        transactionService.saveDepositTransaction(transaction);
    }

    private int accountGen() {
        return ++nextAccountNumber;
    }


}
