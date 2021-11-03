package com.sg.bank.account.domain;

import io.rabitka.core.ddd.IValueType;
import io.rabitka.core.ddd.ValueType;
import io.rabitka.core.metatype.IntegerMetatype;

import java.time.LocalDate;

@ValueType
public class Operation implements IValueType {

    private final Type type;
    private final LocalDate date;
    @IntegerMetatype(onlyPositive = true)
    private final Integer amount;
    @IntegerMetatype(onlyPositive = true)
    private final Integer balance;

    public Operation(Type type, LocalDate date, int amount, int balance) {
        this.type = type;
        this.date = date;
        this.amount = amount;
        this.balance = balance;
        this.validate();
    }

    public Type getType() {
        return type;
    }

    public LocalDate getDate() {
        return date;
    }

    public int getAmount() {
        return amount;
    }

    public int getBalance() {
        return balance;
    }

    @Override
    public void validate() {
        OperationValidator.validate(this);
    }

    @Override
    public boolean equals(Object o) {
        return OperationBase.equals(this, o);
    }

    @Override
    public int hashCode() {
        return OperationBase.hashCode(this);
    }

    public enum Type {
        WITHDRAWAL, DEPOSIT
    }

}
