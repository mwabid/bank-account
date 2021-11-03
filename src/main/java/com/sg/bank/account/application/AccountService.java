package com.sg.bank.account.application;

import com.sg.bank.account.domain.*;
import io.rabitka.core.ddd.ApplicationService;

import java.util.List;

;

@ApplicationService
public class AccountService {

    private com.sg.bank.account.domain.AccountRepository accountRepository;
    private AuthenticationService authenticationService;
    private ConfigService configService;
    private ClockService clockService;

    public AccountService(
            com.sg.bank.account.domain.AccountRepository accountRepository,
            AuthenticationService authenticationService,
            ConfigService configService, ClockService clockService) {
        this.accountRepository = accountRepository;
        this.authenticationService = authenticationService;
        this.configService = configService;
        this.clockService = clockService;
    }

    public void deposit(int amount) {

        this.accountRepository.findBy(AccountPredicates.ownerIdIsEqualsTo(
                getAuthenticatedClientId()
        )).updateEach(
                account -> account.deposit(
                        amount,
                        configService.getMinimumDepositAmount(),
                        clockService)
        );
    }

    public void withdrawal(int amount) throws InsufficientBalanceException {
        Account account = this.accountRepository.findBy(AccountPredicates.ownerIdIsEqualsTo(
                getAuthenticatedClientId()
        )).getInList().get(0);
        account.withdrawal(amount, clockService);
        this.accountRepository.put(account);
    }

    public List<Operation> getHistory() {
        return this.accountRepository.findBy(AccountPredicates.ownerIdIsEqualsTo(
                getAuthenticatedClientId()
        )).getInList().get(0).getOperations();
    }

    private Client.Id getAuthenticatedClientId() {
        return this.authenticationService.getAuthenticatedClient().orElseThrow(
                () -> new IllegalStateException("client should be authenticated")
        ).getId();
    }

}
