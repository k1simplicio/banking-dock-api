package com.api.bankingdock.repositories;

import com.api.bankingdock.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClientsRepository extends JpaRepository<Client, UUID> {
    Optional<Client> findByCpf(String cpf);
}
