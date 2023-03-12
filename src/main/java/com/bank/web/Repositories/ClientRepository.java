package com.bank.web.Repositories;

import com.bank.web.Models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ClientRepository extends JpaRepository<Client, Long> {

    @Query("SELECT u FROM Client u WHERE u.nif =?1")
    Client findByNif(String nif);
    Client findByEmail(String email);

    List<Client> findAll();

}
