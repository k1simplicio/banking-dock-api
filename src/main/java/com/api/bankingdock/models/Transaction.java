package com.api.bankingdock.models;

import javax.persistence.*;

import java.time.Instant;
import java.util.UUID;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "TRANSACTION")
public class Transaction {

    @Id
    @Column(nullable = false)
    private final UUID id = UUID.randomUUID();

    @ManyToOne
    @JoinColumn
    private Account account;
    @Column(nullable = false)
    private double value;

    @Column(nullable = false)
    private final Instant transactionDate = Instant.now();

    @Column(nullable = false)
    private Type type;

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public UUID getId() {
        return id;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public Instant getTransactionDate() {
        return transactionDate;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public enum Type {
        SAQUE, DEPOSITO;
    }
}
