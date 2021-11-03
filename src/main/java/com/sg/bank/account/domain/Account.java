package com.sg.bank.account.domain;

import com.sg.bank.account.application.ClockService;
import io.rabitka.core.ddd.AggregateRoot;
import io.rabitka.core.ddd.IEntity;
import io.rabitka.core.ddd.IdentityAnnotation;
import io.rabitka.core.ddd.UniqueValueIdentity;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.unmodifiableList;

@AggregateRoot
public class Account implements IEntity<Account.Id> {

    private Account.Id id;
    private Client.Id ownerId;
    private List<Operation> operations;

    public Account(Id id, Client.Id ownerId) {
        this.id = id;
        this.ownerId = ownerId;
        this.operations = new LinkedList<>();
        this.validate();
    }

    public Account(Id id, Client.Id ownerId, List<Operation> operations) {
        this.id = id;
        this.ownerId = ownerId;
        this.operations = operations;
        this.validate();
    }

    @Override
    public Account.Id getId() {
        return this.id;
    }

    public Client.Id getOwnerId() {
        return ownerId;
    }

    public List<Operation> getOperations() {
        return unmodifiableList(this.operations);
    }

    @Override
    public void validate() {
        AccountValidator.validate(this);
    }

    public void deposit(int amount, Integer minimumDepositAmount, ClockService clockService) {
        if (amount < minimumDepositAmount) {
            throw new DepositAmountLessThanTheMinimumException(
                    String.format("Cannot make deposit of %s, minimum required is %s",
                            amount,
                            minimumDepositAmount
                    )
            );
        }
        addOperation(
                new Operation(
                        Operation.Type.DEPOSIT,
                        clockService.dateNow(),
                        amount,
                        getBalance() + amount));
    }

    public void withdrawal(int amount, ClockService clockService) throws InsufficientBalanceException {
        if (amount > getBalance()) {
            throw new InsufficientBalanceException(
                    String.format(
                            "Cannot withdrawal the amount of %s, your balance account is insufficient %s euro",
                            amount,
                            getBalance()
                            )
            );
        }
        addOperation(
                new Operation(
                        Operation.Type.WITHDRAWAL,
                        clockService.dateNow(),
                        amount,
                        getBalance() - amount));
    }

    private void addOperation(Operation operation) {
        this.operations.add(
                0,
                operation
        );
    }

    public int getBalance() {
        return getLastOperation()
                .map(Operation::getBalance)
                .orElse(0);
    }

    private Optional<Operation> getLastOperation() {
        if (this.operations.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(this.operations.get(0));
    }

    @IdentityAnnotation
    public static class Id extends UniqueValueIdentity {
        public Id(String value) {
            super(value);
        }

        public Id() {
        }

        @Override
        public String toString() {
            return getValue();
        }
    }

}
