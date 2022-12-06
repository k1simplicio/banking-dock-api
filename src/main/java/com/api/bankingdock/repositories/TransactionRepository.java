package com.api.bankingdock.repositories;

import com.api.bankingdock.models.Account;
import com.api.bankingdock.models.Client;
import com.api.bankingdock.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    List<Transaction> findByAccount(Optional<Account> account);

}
