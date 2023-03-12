package com.bank.web.Service;

import com.bank.web.Models.Client;

import java.util.List;
import java.util.Set;

public interface ClientService {

    Client findByNif(String nif);

    Client findByEmail(String email);


    List<Client> findClientList();
}
