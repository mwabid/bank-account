package com.sg.bank.account.application;

import com.sg.bank.account.domain.Client;

import java.util.Optional;

public interface AuthenticationService {

    void authenticate(Client client);

    Optional<Client> getAuthenticatedClient();

}
