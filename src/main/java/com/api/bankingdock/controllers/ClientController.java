package com.api.bankingdock.controllers;

import com.api.bankingdock.dtos.ClientDto;
import com.api.bankingdock.models.Account;
import com.api.bankingdock.models.Client;
import com.api.bankingdock.services.ClientService;
import com.api.bankingdock.utils.CPFVerifier;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("client-banking")
public class ClientController {
    @Autowired
    ClientService clientService;

    public Client copyClient(ClientDto clientDto) {
        var clientModel = new Client();
        BeanUtils.copyProperties(clientDto, clientModel);
        return clientModel;
    }

    @PostMapping
    public ResponseEntity<Object> createClient(@RequestBody @Valid ClientDto clientDto) {
        try {
            CPFVerifier cpfverified = new CPFVerifier(clientDto.getCpf());
            var clientModel = copyClient(clientDto);
            clientModel.setCpf(cpfverified.getCpf());
            return ResponseEntity.status(HttpStatus.CREATED).body(clientService.save(clientModel));
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }

    @GetMapping
    public ResponseEntity<List<Client>> getAllClients() {
        return ResponseEntity.status(HttpStatus.OK).body(clientService.findAll());
    }

    @GetMapping("/{cpf}")
    public ResponseEntity<Object> getOneClient(@PathVariable(value = "cpf") String cpf) {
        Optional<Client> clientOptional = clientService.findByCpf(cpf);

        if (clientOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Client not found.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(clientOptional.get());
    }

    @DeleteMapping("/{cpf}")
    public ResponseEntity<Object> deleteClients(@PathVariable(value = "cpf") UUID id) {
        Optional<Client> clientOptional = clientService.findById(id);
        if (clientOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Client not found.");
        }
        clientService.delete(clientOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body("Account deleted successfully.");
    }

}
