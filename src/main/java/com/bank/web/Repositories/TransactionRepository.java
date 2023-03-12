package com.bank.web.Repositories;

import com.bank.web.Models.Client;
import com.bank.web.Models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findAll();

}
