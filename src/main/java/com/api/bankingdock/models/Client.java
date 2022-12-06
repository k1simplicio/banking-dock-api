package com.api.bankingdock.models;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "CLIENT")
public class Client implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column(nullable = false)
    private String completeName;
    @Column(nullable = false, unique = true)
    private String cpf;

    @Column(nullable = false)
    private String birthday;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setCompleteName(String completeName) {
        this.completeName = completeName;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getCompleteName() {
        return completeName;
    }

    public String getCpf() {
        return cpf;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
}

