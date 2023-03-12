package com.bank.web.Service;

import com.bank.web.Models.Account;
import com.bank.web.Models.Recipient;
import com.bank.web.Models.Transaction;

import java.security.Principal;
import java.util.List;

public interface TransactionService {
    List<Transaction> findTransactionList(String username);

    void saveDepositTransaction(Transaction primaryTransaction);

    void saveWithdrawTransaction(Transaction primaryTransaction);

    List<Recipient> findRecipientList(Principal principal);

    Recipient saveRecipient(Recipient recipient);

    Recipient findRecipientByName(String recipientName);

    void deleteRecipientByName(String recipientName);

    void transferToSomeoneElse(Recipient recipient, String accountType, String amount, Account account) ;
}
