package com.sg.bank.account.application;

public interface ConfigService {
    void setMinimumDepositAmount(int minimumDepositAmount);

    Integer getMinimumDepositAmount();
}
