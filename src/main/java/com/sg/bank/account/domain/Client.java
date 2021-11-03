package com.sg.bank.account.domain;

import io.rabitka.core.ddd.*;
import io.rabitka.core.metatype.StringMetatype;

@AggregateRoot
public class Client implements IEntity<Client.Id> {

    private Client.Id id;
    @StringMetatype(regex = "[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+")
    private String emailAddress;

    public Client(Id id, String emailAddress) {
        this.id = id;
        this.emailAddress = emailAddress;
        this.validate();
    }

    @Override
    public Id getId() {
        return this.id;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    @Override
    public void validate() {
        ClientValidator.validate(this);
    }

    @IdentityAnnotation
    public static class Id extends UniqueValueIdentity {
        public Id(String value) {super(value);}
        public Id() {}

        @Override
        public String toString() {
            return getValue();
        }
    }

}
