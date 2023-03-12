package com.bank.web.Service.ServiceImpl;

import com.bank.web.Models.Client;
import com.bank.web.Repositories.ClientRepository;
import com.bank.web.Service.AccountService;
import com.bank.web.Service.ClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientServiceImpl  implements ClientService {

    private static final Logger LOG = LoggerFactory.getLogger(ClientService.class);

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private AccountService accountService;

    public void save(Client client) {
        clientRepository.save(client);
    }

    public Client findByNif(String nif) {
        return clientRepository.findByNif(nif);
    }

    public Client findByEmail(String email) {
        return clientRepository.findByEmail(email);
    }


    public Client createClient(Client client) {
        Client localClient = clientRepository.findByNif(client.getNif());

        if (localClient != null) {
            LOG.info("User with username {} already exist. Nothing will be done. ", client.getNif());
        } else {
            String encryptedPassword = passwordEncoder.encode(client.getPassword());
            client.setPassword(encryptedPassword);


            client.setAccount(accountService.createAccount());

            localClient = clientRepository.save(client);
        }

        return localClient;
    }


    public List<Client> findClientList() {
        return clientRepository.findAll();
    }

}
