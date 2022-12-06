package com.api.bankingdock.services;

import com.api.bankingdock.models.Account;
import com.api.bankingdock.models.Client;
import com.api.bankingdock.repositories.ClientsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ClientService {
    @Autowired
    ClientsRepository clientsRepository;

    @Transactional
    public Client save(Client clientModel) {
        return clientsRepository.save(clientModel);
    }

    public Optional<Client> findByCpf(String Cpf) {
        return clientsRepository.findByCpf(Cpf);
    }

    public List<Client> findAll() {
        return clientsRepository.findAll();
    }

    public Optional<Client> findById(UUID id) {
        return clientsRepository.findById(id);
    }

    @Transactional
    public void delete(Client client) {
        clientsRepository.delete(client);
    }
}
