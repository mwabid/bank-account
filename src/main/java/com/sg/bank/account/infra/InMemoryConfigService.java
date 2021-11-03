package com.sg.bank.account.infra;

import com.sg.bank.account.application.ConfigService;
import com.sg.bank.account.domain.Amount;
import org.springframework.stereotype.Service;

@Service
public class InMemoryConfigService implements ConfigService {
    private Amount minimumDepositAmount;

    @Override
    public void setMinimumDepositAmount(Amount minimumDepositAmount) {
        this.minimumDepositAmount = minimumDepositAmount;
    }

    public Amount getMinimumDepositAmount() {
        return minimumDepositAmount;
    }
}
