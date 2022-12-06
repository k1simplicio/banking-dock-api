package com.api.bankingdock.controllers.app;

import com.api.bankingdock.models.Account;
import com.api.bankingdock.models.Transaction;
import com.api.bankingdock.repositories.AccountsRepository;
import com.api.bankingdock.repositories.ClientsRepository;
import com.api.bankingdock.repositories.TransactionRepository;
import com.api.bankingdock.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.Instant;
import java.util.*;




@Controller
public class TransactionControllerApp {
    @Autowired
    AccountsRepository accountsRepository;
    @Autowired
    ClientsRepository clientsRepository;
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    AccountService accountService;

    public boolean checkCurrentTransactionDate(UUID id) {
        Optional<Transaction> transaction = transactionRepository.findById(id);
        return transaction.map(value -> value.getTransactionDate().toString().contains(Instant.now().toString().substring(0, 10))).orElse(false);
    }

    public void withdraw(Account account, Transaction transaction) {
        var valueChecker = (account.getAmount() - transaction.getValue());

        if (valueChecker >= 0 && account.getDailyCount() + transaction.getValue() <= account.getDailyLimit()) {
            account.setAmount(valueChecker);
            account.setDailyCount(dailyValue(account));
            transactionRepository.save(transaction);
            accountService.save(account);
        }
    }

    public void deposit(Account account, Transaction transaction) {
        account.setAmount(account.getAmount() + transaction.getValue());
        transactionRepository.save(transaction);
    }

    public double dailyValue(Account account) {
        double dailyValue = 0;
        List<Transaction> transactions = transactionRepository.findByAccount(Optional.ofNullable(account));
        for (Transaction currentTransaction : transactions) {
            if (checkCurrentTransactionDate(currentTransaction.getId()) &&
                    currentTransaction.getType().toString().equals("SAQUE")) {
                dailyValue += currentTransaction.getValue();
            }
        }
        return dailyValue;

    }

    public void doTransaction(Account account, Transaction transaction) {
        if (account.getIsActive()) {
            if (transaction.getType().toString().equals("DEPOSITO")) {
                deposit(account, transaction);

            }
            if (transaction.getType().toString().equals("SAQUE")) {
                withdraw(account, transaction);
            }
        }
    }

    @RequestMapping(value = "/saveTransaction", method = RequestMethod.POST)
    public String saveTransaction(@RequestParam(value = "id") UUID id, Transaction transaction) {
        Optional<Account> accountOptional = accountsRepository.findById(id);
        Account account = accountOptional.get();
        transaction.setAccount(account);
        doTransaction(account, transaction);

        return "redirect:/home?id=" + account.getId();
    }

}
