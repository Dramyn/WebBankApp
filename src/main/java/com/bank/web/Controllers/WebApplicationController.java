package com.bank.web.Controllers;

import com.bank.web.Models.Recipient;
import com.bank.web.Repositories.AccountRepository;
import com.bank.web.Models.Client;
import com.bank.web.Models.Account;
import com.bank.web.Repositories.ClientRepository;
import com.bank.web.Service.ClientService;
import com.bank.web.Service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;


@Controller
public class WebApplicationController {


    @Autowired
    private ClientRepository clientRepo;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private ClientService clientService;


    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/register")
    public String showSignUpForm(Model model) {
        model.addAttribute("client", new Client());
        return "signup_form";
    }
    @PostMapping("/process_register")
    public String processRegister(Client client) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(client.getPassword());
        client.setPassword(encodedPassword);
        clientRepo.save(client);

        return "register_success";
    }

    @GetMapping("/list_users")
    public String viewAllUsers(Model model) {
        List<Client> listusers = clientRepo.findAll();
        model.addAttribute("listusers", listusers);
        return "users";
    }

    @GetMapping("/dashboard")
    public ModelAndView showDashboard(Model model) {
        model.addAttribute("client", new Client());
        ModelAndView dashboardPage = new ModelAndView("dashboard");


        return dashboardPage;
    }

    @RequestMapping(value = "/recipient", method = RequestMethod.GET)
    public String recipient(Model model, Principal principal) {
        List<Recipient> recipientList = transactionService.findRecipientList(principal);

        Recipient recipient = new Recipient();

        model.addAttribute("recipientList", recipientList);
        model.addAttribute("recipient", recipient);

        return "recipient";
    }

    @RequestMapping(value = "/recipient/save", method = RequestMethod.POST)
    public String recipientPost(@ModelAttribute("recipient") Recipient recipient, Principal principal) {

        Client client = clientService.findByNif(principal.getName());
        recipient.setClient(client);
        transactionService.saveRecipient(recipient);

        return "redirect:/transfer/recipient";
    }

    @RequestMapping(value = "/recipient/edit", method = RequestMethod.GET)
    public String recipientEdit(@RequestParam(value = "recipientName") String recipientName, Model model, Principal principal){

        Recipient recipient = transactionService.findRecipientByName(recipientName);
        List<Recipient> recipientList = transactionService.findRecipientList(principal);

        model.addAttribute("recipientList", recipientList);
        model.addAttribute("recipient", recipient);

        return "recipient";
    }

    @RequestMapping(value = "/recipient/delete", method = RequestMethod.GET)
    @Transactional
    public String recipientDelete(@RequestParam(value = "recipientName") String recipientName, Model model, Principal principal){

        transactionService.deleteRecipientByName(recipientName);

        List<Recipient> recipientList = transactionService.findRecipientList(principal);

        Recipient recipient = new Recipient();
        model.addAttribute("recipient", recipient);
        model.addAttribute("recipientList", recipientList);


        return "recipient";
    }

    @RequestMapping(value = "/toSomeoneElse",method = RequestMethod.GET)
    public String toSomeoneElse(Model model, Principal principal) {
        List<Recipient> recipientList = transactionService.findRecipientList(principal);

        model.addAttribute("recipientList", recipientList);
        model.addAttribute("accountType", "");

        return "toSomeoneElse";
    }

    @RequestMapping(value = "/toSomeoneElse",method = RequestMethod.POST)
    public String toSomeoneElsePost(@ModelAttribute("recipientName") String recipientName, @ModelAttribute("accountType") String accountType, @ModelAttribute("amount") String amount, Principal principal) {
        Client client = clientService.findByNif(principal.getName());
        Recipient recipient = transactionService.findRecipientByName(recipientName);
        transactionService.transferToSomeoneElse(recipient, accountType, amount, client.getAccount());

        return "redirect:/dashboard";
    }



}
