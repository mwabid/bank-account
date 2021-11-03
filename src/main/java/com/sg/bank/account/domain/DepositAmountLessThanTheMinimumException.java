package com.sg.bank.account.domain;

public class DepositAmountLessThanTheMinimumException extends RuntimeException {
    public DepositAmountLessThanTheMinimumException(String message) {
        super(message);
    }
}
