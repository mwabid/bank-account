/*
 *
 * Copyright (c) 2017 Rabitka Framework
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.sg.bank.account.domain;

import io.rabitka.core.ddd.IValueType;
import io.rabitka.core.ddd.ValueType;
import io.rabitka.core.metatype.IntegerMetatype;
import io.rabitka.core.metatype.StringMetatype;
import io.rabitka.validator.core.InvalidDomainObjectException;

import java.math.BigDecimal;
import java.util.Objects;

import static java.lang.String.format;
import static java.math.BigDecimal.valueOf;

@ValueType
public class Amount implements IValueType {

    public static class CurrencyException extends RuntimeException {
        public CurrencyException(String msg) {
            super(msg);
        }
    }

    public enum Currency {
        EURO
    }

    private BigDecimal value;
    private Currency currency;

    public static Amount inEur(int value) {
        return new Amount(new BigDecimal(value), Currency.EURO);
    }

    public Amount(BigDecimal value, Currency currency) {
        this.value = value;
        this.currency = currency;
        validate();
    }

    private Amount(){}

    public Amount add(Amount amount) {
        if (haveDifferentCurrencyThan(amount)) {
            throw new CurrencyException(format(
                    "Cannot sum amounts having different currencies : " +
                    "cannot add %s to %s", amount, this));
        }
        return new Amount(this.value.add(amount.value), this.currency);
    }

    public Amount subtract(Amount amount) {
        if (haveDifferentCurrencyThan(amount)) {
            throw new CurrencyException(format(
                    "Cannot subtract amounts having different currencies : " +
                            "cannot subtract %s from %s", amount, this));
        }
        return new Amount(this.value.subtract(amount.value), this.currency);
    }

    public boolean isLessThan(Amount amount) {
        if (haveDifferentCurrencyThan(amount)) {
            throw new CurrencyException(format("Cannot compare amounts having different currencies : cannot compare %s to %s", amount, this.toString()));
        }
        return this.value.compareTo(amount.value) < 0;
    }

    private boolean haveDifferentCurrencyThan(Amount amount) {
        return !amount.currency.equals(this.currency);
    }

    public Currency getCurrency() {
        return currency;
    }

    public BigDecimal getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Amount{" +
                "value=" + value +
                ", currency='" + currency + '\'' +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        return AmountBase.equals(this, o);
    }

    @Override
    public int hashCode() {
        return AmountBase.hashCode(this);
    }

    @Override
    public void validate() {

        AmountValidator.validate(this);
    }

}