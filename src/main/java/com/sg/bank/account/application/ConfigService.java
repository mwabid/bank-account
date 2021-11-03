package com.sg.bank.account.application;

import com.sg.bank.account.domain.Amount;

public interface ConfigService {
    void setMinimumDepositAmount(Amount minimumDepositAmount);

    Amount getMinimumDepositAmount();
}
