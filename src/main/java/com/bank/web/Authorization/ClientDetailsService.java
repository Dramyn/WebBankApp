package com.bank.web.Authorization;

import com.bank.web.Models.Client;
import com.bank.web.Repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


public class ClientDetailsService implements UserDetailsService {

    @Autowired
    private ClientRepository ClientRepo;
    @Override
    public UserDetails loadUserByUsername(String nif) throws UsernameNotFoundException {
        Client client = ClientRepo.findByNif(nif);
        if (client == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return new ClientDetails(client);
    }
}
