package com.bank.web.Service.ServiceImpl;

import com.bank.web.Models.Account;
import com.bank.web.Models.Client;
import com.bank.web.Models.Recipient;
import com.bank.web.Models.Transaction;
import com.bank.web.Repositories.AccountRepository;
import com.bank.web.Repositories.RecipientRepository;
import com.bank.web.Repositories.TransactionRepository;
import com.bank.web.Service.ClientService;
import com.bank.web.Service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private ClientService clientService;

    @Autowired
    private TransactionRepository transactionRepository;


    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private RecipientRepository recipientRepository;


    public List<Transaction> findTransactionList(String nif){
        Client client = clientService.findByNif(nif);
        List<Transaction> transactionList = client.getAccount().getTransactionsList();

        return transactionList;
    }

    public void saveDepositTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
    }


    public void saveWithdrawTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
    }


    public List<Recipient> findRecipientList(Principal principal) {
        String username = principal.getName();
        List<Recipient> recipientList = recipientRepository.findAll().stream() 			//convert list to stream
                .filter(recipient -> username.equals(recipient.getClient().getNif()))	//filters the line, equals to username
                .collect(Collectors.toList());

        return recipientList;
    }

    public Recipient saveRecipient(Recipient recipient) {
        return recipientRepository.save(recipient);
    }

    public Recipient findRecipientByName(String recipientName) {
        return recipientRepository.findByName(recipientName);
    }

    public void deleteRecipientByName(String recipientName) {
        recipientRepository.deleteByName(recipientName);
    }

    public void transferToSomeoneElse(Recipient recipient, String accountType, String amount, Account account) {

            account.setBalance(account.getBalance().subtract(new BigDecimal(amount)));
            accountRepository.save(account);

            Date date = new Date();

            Transaction transaction = new Transaction(date,"Transfer", "Finished", Double.parseDouble(amount), account.getBalance(), account);
            transactionRepository.save(transaction);

    }
}

