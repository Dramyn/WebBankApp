package com.bank.web.Controllers;


import com.bank.web.Models.Account;
import com.bank.web.Models.Client;
import com.bank.web.Models.Transaction;
import com.bank.web.Repositories.AccountRepository;
import com.bank.web.Service.AccountService;
import com.bank.web.Service.ClientService;
import com.bank.web.Service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;


@Controller
public class AccountController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionService transactionService;

    @RequestMapping("/account")
    public String primaryAccount(Model model, Principal principal) {
        List<Transaction> transactionList = transactionService.findTransactionList(principal.getName());

        Client client = clientService.findByNif(principal.getName());
        Account account = client.getAccount();

        model.addAttribute("Account", account);
        model.addAttribute("TransactionList", transactionList);

        return "accounts";
    }



    @RequestMapping(value = "/deposit", method = RequestMethod.GET)
    public String deposit(Model model) {
        model.addAttribute("accountType", "");
        model.addAttribute("amount", "");

        return "deposit";
    }

    @RequestMapping(value = "/deposit", method = RequestMethod.POST)
    public String depositPOST(@ModelAttribute("amount") String amount, @ModelAttribute("accountType") String accountType, Principal principal) {
        accountService.deposit(accountType, Double.parseDouble(amount), principal);

        return "redirect:/dashboard";
    }

    @RequestMapping(value = "/withdraw", method = RequestMethod.GET)
    public String withdraw(Model model) {
        model.addAttribute("accountType", "");
        model.addAttribute("amount", "");

        return "withdraw";
    }

    @RequestMapping(value = "/withdraw", method = RequestMethod.POST)
    public String withdrawPOST(@ModelAttribute("amount") String amount, @ModelAttribute("accountType") String accountType, Principal principal) {
        accountService.withdraw(accountType, Double.parseDouble(amount), principal);

        return "redirect:/dashboard";
    }
}
