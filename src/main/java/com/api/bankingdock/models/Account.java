package com.api.bankingdock.models;

import javax.persistence.*;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "ACCOUNT")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Client client;

    @Column(nullable = false)
    private double amount = 0;

    @Column(nullable = false)
    private boolean isActive = true;

    @Column
    private final double dailyLimit = 1_000;

    @Column(nullable = false)
    private double dailyCount = 0;

    @Column(nullable = false, updatable = false)
    private final Instant dateCreation = Instant.now();

    public Account() {
    }


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Account(Client client) throws ParseException {
        this.client = client;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public double getDailyLimit() {
        return dailyLimit;
    }

    public Instant getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
    }

    public void setDailyCount(double dailyCount) {
        this.dailyCount = dailyCount;
    }

    public double getDailyCount() {
        return dailyCount;
    }
}
