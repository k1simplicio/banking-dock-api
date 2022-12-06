package com.api.bankingdock.services;

import com.api.bankingdock.models.Account;

import com.api.bankingdock.models.Client;
import com.api.bankingdock.repositories.AccountsRepository;
import com.api.bankingdock.repositories.ClientsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public class AccountService {
    @Autowired
    AccountsRepository accountsRepository;
    @Autowired
    ClientsRepository clientsRepository;

    @Transactional
    public Account save(Account accountModel) {
        return accountsRepository.save(accountModel);
    }


    public List<Account> findAll() {
        return accountsRepository.findAll();
    }

    public List<Account> findByClient(String cpf) {
        Optional<Client> client = clientsRepository.findByCpf(cpf);
        return accountsRepository.findByClient(client);
    }

    public Optional<Account> findById(UUID id) {
        return accountsRepository.findById(id);
    }


    @Transactional
    public void delete(Account account) {
        accountsRepository.delete(account);
    }
}
