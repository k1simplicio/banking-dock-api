package com.api.bankingdock.dtos;

import javax.validation.constraints.NotBlank;

public class ClientDto {

    @NotBlank
    private String completeName;

    @NotBlank
    private String cpf;

    private String birthday;

    public String getCompleteName() {
        return completeName;
    }

    public void setCompleteName(String completeName) {
        this.completeName = completeName;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
}
