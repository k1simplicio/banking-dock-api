package com.api.bankingdock.utils;

import br.com.caelum.stella.ValidationMessage;
import br.com.caelum.stella.validation.CPFValidator;

import java.util.List;

public class CPFVerifier {

    private String cpf;

    public CPFVerifier(String cpf) {

        boolean cpfModelChecker = cpfChecker(cpf);
        if (cpfModelChecker) {
            this.cpf = cpf;
        } else {
            System.out.println("Try another cpf");
        }
    }


    private boolean cpfChecker(String cpf) {
        CPFValidator cpfValidator = new CPFValidator();
        List<ValidationMessage> errors = cpfValidator.invalidMessagesFor(cpf);
        if (errors.size() > 0) {
            System.out.println(errors.get(0));
            return false;
        } else {
            return true;
        }
    }

    public String getCpf() {
        return cpf;
    }
}
