package com.sg.bank.account.infra;

import com.sg.bank.account.application.ConfigService;
import org.springframework.stereotype.Service;

@Service
public class InMemoryConfigService implements ConfigService {
    private int minimumDepositAmount;

    @Override
    public void setMinimumDepositAmount(int minimumDepositAmount) {
        this.minimumDepositAmount = minimumDepositAmount;
    }

    public Integer getMinimumDepositAmount() {
        return minimumDepositAmount;
    }
}
