package com.api.bankingdock.repositories;

import com.api.bankingdock.models.Account;
import com.api.bankingdock.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountsRepository extends JpaRepository<Account, UUID> {
    List<Account> findByClient(Optional<Client> client);
}
