package com.api.bankingdock.services;

import com.api.bankingdock.models.Account;
import com.api.bankingdock.models.Transaction;
import com.api.bankingdock.repositories.AccountsRepository;
import com.api.bankingdock.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class TransactionService {
    @Autowired
    AccountsRepository accountsRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Transactional
    public Transaction save(Transaction transaction) {
        return transactionRepository.save(transaction);
    }


    public List<Transaction> findAll() {
        return transactionRepository.findAll();
    }

    public Optional<Transaction> findById(UUID id) {
        return transactionRepository.findById(id);
    }
}
