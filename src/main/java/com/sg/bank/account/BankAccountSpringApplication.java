package com.sg.bank.account;

import com.sg.bank.account.application.AuthenticationService;
import com.sg.bank.account.application.ConfigService;
import com.sg.bank.account.domain.Account;
import com.sg.bank.account.domain.AccountRepository;
import com.sg.bank.account.domain.Client;
import com.sg.bank.account.domain.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import static com.sg.bank.account.domain.ClientPredicates.emailAddressIsEqualsTo;

@SpringBootApplication
public class BankAccountSpringApplication {

    @Autowired private ConfigService configService;
    @Autowired private AuthenticationService authenticationService;
    @Autowired private ClientRepository clientRepository;
    @Autowired private AccountRepository accountRepository;

    public static void main(String[] args) {
        SpringApplication.run(BankAccountSpringApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void init() {

        final int minimumDepositAmount = 10;
        final String emailAddress = "abc@sg.com";

        this.configService.setMinimumDepositAmount(minimumDepositAmount);
        this.clientRepository.put(
                new Client(new Client.Id(), emailAddress)
        );
        Client owner = this.clientRepository
                .findBy(emailAddressIsEqualsTo(emailAddress))
                .getInList().get(0);
        this.accountRepository.put(
                new Account(new Account.Id(), owner.getId())
        );
        this.authenticationService.authenticate(owner);
    }

}
