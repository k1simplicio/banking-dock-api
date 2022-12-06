package com.api.bankingdock.controllers;

import com.api.bankingdock.dtos.ClientDto;
import com.api.bankingdock.models.Account;
import com.api.bankingdock.models.Client;
import com.api.bankingdock.services.AccountService;
import com.api.bankingdock.services.ClientService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/account-banking")
public class AccountController {
    @Autowired
    AccountService accountService;

    @Autowired
    ClientService clientService;

    public Client clientFromClientDto(ClientDto clientDto) {
        var clientModel = new Client();
        BeanUtils.copyProperties(clientDto, clientModel);
        return clientModel;
    }

    @PostMapping
    public ResponseEntity<Object> accountCreation(@RequestBody @Valid ClientDto clientDto) {
        Optional<Client> optionalClient = clientService.findByCpf(clientDto.getCpf());
        try {
            if (optionalClient.isPresent()) {
                Client client = optionalClient.get();
                System.out.println(client.getCpf());
                if (Objects.equals(client.getCompleteName(), clientDto.getCompleteName())) {
                    Account accountModel = new Account(client);
                    accountModel.setDateCreation(LocalDateTime.now(ZoneId.of("UTC")));
                    return ResponseEntity.status(HttpStatus.CREATED).body(accountService.save(accountModel));
                }
            }
        } catch (Exception e) {

            throw new RuntimeException(e);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(accountService);
    }

    @GetMapping
    public ResponseEntity<List<Account>> getAllAccounts() {
        return ResponseEntity.status(HttpStatus.OK).body(accountService.findAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteAccount(@PathVariable(value = "id") UUID id) {
        Optional<Account> accountOptional = accountService.findById(id);
        if (accountOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found.");
        }
        accountService.delete(accountOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body("Account deleted successfully.");
    }


    @GetMapping("/{cpf}")
    public ResponseEntity<Object> getAccountsByCpf(@PathVariable(value = "cpf") String cpf) {
        List<Account> accountOptional = accountService.findByClient(cpf);

        if (accountOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("List of accounts not found.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(accountOptional);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> updateAccount(@PathVariable(value = "id") UUID id,
                                                @RequestBody @Valid Account account) {
        Optional<Account> accountOptional = accountService.findById(id);
        if (accountOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found.");
        }
        var accountModel = new Account();
        BeanUtils.copyProperties(account, accountModel);
        return ResponseEntity.status(HttpStatus.OK).body(accountService.save(accountModel));
    }
}

