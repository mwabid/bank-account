package com.sg.bank.account.domain;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.math.BigDecimal;

import static com.sg.bank.account.domain.Amount.Currency.USD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@RunWith(JUnit4.class)
public class AmountTest {

    public static final Amount TEN_EUR = Amount.inEur(10);
    public static final Amount FIVE_EUR = Amount.inEur(5);
    public static final Amount TEN_USD = new Amount(new BigDecimal(10), USD);

    @Test
    public void should_sum_amounts() {
        assertThat(
                FIVE_EUR.add(FIVE_EUR)
        ).isEqualTo(TEN_EUR);
    }

    @Test
    public void should_fail_when_summing_different_currencies() {
        assertThatThrownBy(
                () -> TEN_EUR.add(TEN_USD)
        ).isInstanceOf(Amount.CurrencyException.class)
                .hasMessage("Cannot sum amounts having different currencies : " +
                        "cannot add %s to %s", TEN_USD, TEN_EUR);
    }

    @Test
    public void should_subtracting_amounts() {
        assertThat(
                TEN_EUR.subtract(FIVE_EUR)
        ).isEqualTo(FIVE_EUR);
    }

    @Test
    public void should_fail_when_subtracting_different_currencies() {
        assertThatThrownBy(
                () -> TEN_EUR.subtract(TEN_USD)
        ).isInstanceOf(Amount.CurrencyException.class)
                .hasMessage("Cannot subtract amounts having different currencies : " +
                        "cannot subtract %s from %s", TEN_USD, TEN_EUR);
    }

    @Test
    public void should_compare_amounts() {
        assertThat(FIVE_EUR.isLessThan(TEN_EUR)).isTrue();
        assertThat(TEN_EUR.isLessThan(FIVE_EUR)).isFalse();
        assertThat(TEN_EUR.isLessThan(TEN_EUR)).isFalse();
    }

    @Test
    public void should_fail_when_comparing_different_currencies() {
        assertThatThrownBy(
                () -> TEN_EUR.isLessThan(TEN_USD)
        ).isInstanceOf(Amount.CurrencyException.class)
                .hasMessage("Cannot compare amounts having different currencies : " +
                        "cannot compare %s to %s", TEN_USD, TEN_EUR);
    }


}