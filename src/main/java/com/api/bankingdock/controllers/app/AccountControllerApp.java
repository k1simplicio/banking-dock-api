package com.api.bankingdock.controllers.app;

import com.api.bankingdock.models.Account;
import com.api.bankingdock.models.Client;
import com.api.bankingdock.models.Transaction;
import com.api.bankingdock.repositories.AccountsRepository;
import com.api.bankingdock.repositories.ClientsRepository;
import com.api.bankingdock.repositories.TransactionRepository;
import com.api.bankingdock.services.AccountService;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Controller
public class AccountControllerApp {
    @Autowired
    AccountsRepository accountsRepository;
    @Autowired
    ClientsRepository clientsRepository;
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    AccountService accountService;

    public List<Transaction> transfersFromAccount(UUID id) {
        Optional<Account> accountOptional = accountsRepository.findById(id);
        return transactionRepository.findByAccount(accountOptional);
    }

    public List<Transaction> transfersFromDate(UUID id, String start, String end) {
        Optional<Account> accountOptional = accountsRepository.findById(id);
        List<Transaction> transfers = transactionRepository.findByAccount(accountOptional);
        List<Transaction> transactionsFromDate = new ArrayList<>();
        for (Transaction transfer : transfers) {
            if (transfer.getTransactionDate().isAfter(Instant.parse(start.replaceAll(",", "") + "T00:00:00.00Z")) &&
                    transfer.getTransactionDate().isBefore(Instant.parse(end.replaceAll(",", "") + "T23:59:59.999999999Z"))) {
                transactionsFromDate.add(transfer);
            }
        }

        return transactionsFromDate;
    }

    @RequestMapping("/{cpf}")
    public ModelAndView accountsList(@PathVariable("cpf") String cpf) {
        Optional<Client> client = clientsRepository.findByCpf(cpf);
        List<Account> accounts = accountsRepository.findByClient(client);
        ModelAndView mv = new ModelAndView("accountsList");
        mv.addObject("accounts", accounts);
        return mv;
    }

    @RequestMapping(value = "/{cpf}", method = RequestMethod.POST)
    public String accountCreation(@PathVariable("cpf") String cpf) {
        var optionalClient = clientsRepository.findByCpf(cpf);
        try {
            if (optionalClient.isPresent()) {
                Client client = optionalClient.get();
                Account account = new Account(client);
                account.setDateCreation(LocalDateTime.now(ZoneId.of("UTC")));
                accountsRepository.save(account);
                return "redirect:/{cpf}";
            }
        } catch (Exception e) {

            throw new RuntimeException(e);
        }
        return "redirect:/{cpf}";
    }

    @RequestMapping(value = "/delete-account/{id}")
    public String deleteAccount(@PathVariable("id") UUID id) {
        Optional<Account> accountOptional = accountsRepository.findById(id);
        Account account = accountOptional.get();
        List<Transaction> transactions = transfersFromAccount(id);
        transactionRepository.deleteAll(transactions);
        accountService.delete(account);
        return "redirect:/" + account.getClient().getCpf();
    }

    @RequestMapping(value = "/change-account-status/{id}")
    public String changeAccountStatus(@PathVariable("id") UUID id) {
        Optional<Account> accountOptional = accountsRepository.findById(id);
        Account account = accountOptional.get();
        account.setIsActive(!account.getIsActive());
        accountService.save(account);
        return "redirect:/home?id=" + account.getId().toString();
    }


    @RequestMapping(value = "/home")
    public ModelAndView accountHome(@RequestParam UUID id) {
        Account account = accountsRepository.findById(id).get();
        List<Transaction> transactions = transfersFromAccount(id);
        ModelAndView mv = new ModelAndView("accountHome");

        mv.addObject("clientAccount", account);
        mv.addObject("transactions", transactions);


        return mv;
    }


    @RequestMapping(value = "/searchTransaction", method = RequestMethod.POST)
    public ModelAndView searchTransaction(@RequestParam(value = "id") UUID id, String startDate, String finalDate) {
        List<Transaction> transfersFromDate = transfersFromDate(id, startDate.replaceAll(",", ""), finalDate.replaceAll(",", ""));
        Account account = accountsRepository.findById(id).get();
        List<Transaction> transactions = transfersFromAccount(id);
        ModelAndView mv = new ModelAndView("accountHome");

        mv.addObject("clientAccount", account);
        mv.addObject("transactions", transactions);
        mv.addObject("dateTransactions", transfersFromDate);
        return mv;
    }


}
