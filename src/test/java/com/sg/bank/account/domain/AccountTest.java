package com.sg.bank.account.domain;

import com.sg.bank.account.infra.FixableClockService;
import io.rabitka.validator.core.TypeCheckerFactoryRegistry;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.time.LocalDate;

import static com.sg.bank.account.domain.Operation.Type.DEPOSIT;
import static com.sg.bank.account.domain.Operation.Type.WITHDRAWAL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@RunWith(JUnit4.class)
public class AccountTest {

    private static final Client.Id AN_OWNER_ID = new Client.Id();
    private static final Account.Id AN_ACCOUNT_ID = new Account.Id();
    private static final Amount TEN_EUR = Amount.inEur(10);
    private static final Amount FIVE_EUR = Amount.inEur(5);
    private FixableClockService clockService;
    private Account anAccount;

    @Before
    public void setUp() throws Exception {
        clockService = new FixableClockService();
        anAccount = new Account(AN_ACCOUNT_ID, AN_OWNER_ID);
    }

    @BeforeClass
    public static void beforeClass() throws Exception {
        TypeCheckerFactoryRegistry.addTypeChecker(AmountMetaType.class, new AmountMetatypeValidator.Factory());
    }

    @Test
    public void should_add_amount_to_balance_when_deposit() {
        anAccount.deposit(TEN_EUR, TEN_EUR, clockService);
        assertThat(anAccount.getBalance()).isEqualTo(TEN_EUR);
    }

    @Test
    public void should_fail_when_deposit_amount_less_than_minimum_deposit_amount() {
        final Amount minimumDepositAmount = TEN_EUR;
        final Amount amountToDeposit = FIVE_EUR;
        assertThatThrownBy(
                () ->
                        anAccount.deposit(amountToDeposit, minimumDepositAmount, clockService)
        ).isInstanceOf(DepositAmountLessThanTheMinimumException.class)
                .hasMessage("Cannot make deposit of %s, minimum required is %s",
                        amountToDeposit, minimumDepositAmount);
    }

    @Test
    public void should_subtract_amount_from_balance_when_withdrawn() {
        anAccount.deposit(TEN_EUR, TEN_EUR, clockService);
        anAccount.withdrawal(FIVE_EUR, clockService);
        assertThat(anAccount.getBalance()).isEqualTo(FIVE_EUR);
    }

    @Test(expected = InsufficientBalanceException.class)
    public void should_fail_when_withdrawn_insufficient_balance() {
        anAccount.withdrawal(TEN_EUR, clockService);
    }

    @Test
    public void should_show_history_of_operations() {
        final LocalDate depositDate = LocalDate.parse("2021-11-01");
        final LocalDate withdrawalDate = LocalDate.parse("2021-11-05");
        clockService.setFixedLocalDate(depositDate);
        anAccount.deposit(TEN_EUR, TEN_EUR, clockService);
        clockService.setFixedLocalDate(withdrawalDate);
        anAccount.withdrawal(FIVE_EUR, clockService);
        assertThat(anAccount.getOperations()).containsExactly(
                new Operation(WITHDRAWAL, withdrawalDate, FIVE_EUR, FIVE_EUR),
                new Operation(DEPOSIT, depositDate, TEN_EUR, TEN_EUR)
        );
    }

    @After
    public void tearDown() throws Exception {
        clockService.resetFixedLocalDate();
    }
}