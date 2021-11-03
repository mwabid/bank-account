package com.sg.bank.account.infra;

import com.sg.bank.account.application.AuthenticationService;
import com.sg.bank.account.domain.Client;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class InMemoryAuthenticationService implements AuthenticationService {

    private Client authenticatedClient;

    @Override
    public void authenticate(Client client) {

        this.authenticatedClient = client;
    }

    public Optional<Client> getAuthenticatedClient() {
        return Optional.ofNullable(authenticatedClient);
    }
}
