package com.api.bankingdock.controllers.app;

import com.api.bankingdock.models.Account;
import com.api.bankingdock.models.Client;
import com.api.bankingdock.models.Transaction;
import com.api.bankingdock.repositories.AccountsRepository;
import com.api.bankingdock.repositories.ClientsRepository;
import com.api.bankingdock.repositories.TransactionRepository;
import com.api.bankingdock.services.AccountService;
import com.api.bankingdock.utils.CPFVerifier;
import net.bytebuddy.implementation.bind.annotation.BindingPriority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller

public class ClientControllerApp {

    @Autowired
    AccountService accountService;
    @Autowired
    private ClientsRepository clientsRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private AccountsRepository accountsRepository;

    public List<Account> accountsFromClient(UUID id) {
        Optional<Client> clientOptional = clientsRepository.findById(id);
        return accountsRepository.findByClient(clientOptional);
    }

    public List<Transaction> transfersFromAccount(UUID id) {
        Optional<Account> accountOptional = accountsRepository.findById(id);
        return transactionRepository.findByAccount(accountOptional);
    }

    @RequestMapping(value = "/clients", method = RequestMethod.GET)
    public ModelAndView clientsList() {
        ModelAndView mv = new ModelAndView("clientsList");
        Iterable<Client> clients = clientsRepository.findAll();
        mv.addObject("clients", clients);
        return mv;
    }

    @RequestMapping(value = "/clients", method = RequestMethod.POST)
    public String createClient(@Valid Client client, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("mensagem", "CPF inválido ou já existente");
            return "redirect:/clients";
        }
        CPFVerifier verifiedCpf = new CPFVerifier(client.getCpf());
        client.setCpf(verifiedCpf.getCpf());
        clientsRepository.save(client);
        redirectAttributes.addFlashAttribute("mensagem", "Cliente adicionado com sucesso.");
        return "redirect:/clients";

    }

    @RequestMapping(value = "/delete-client/{id}")
    public String deleteClient(@PathVariable("id") UUID id) {
        Optional<Client> clientOptional = clientsRepository.findById(id);
        Client client = clientOptional.get();
        List<Account> accounts = accountsFromClient(id);
        if (!accounts.isEmpty()) {
            for (Account account : accounts) {
                List<Transaction> transactions = transfersFromAccount(account.getId());
                transactionRepository.deleteAll(transactions);
            }
            accountsRepository.deleteAll(accounts);
        }
        clientsRepository.delete(client);
        return "redirect:/clients";
    }


}
