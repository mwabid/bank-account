package com.sg.bank.account.application;

import com.sg.bank.account.domain.*;
import com.sg.bank.account.infra.FixableClockService;
import com.sg.bank.account.infra.InMemoryAuthenticationService;
import com.sg.bank.account.infra.InMemoryConfigService;
import cucumber.api.DataTable;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.rabitka.repository.provider.memory.InMemoryProvider;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.sg.bank.account.domain.ClientPredicates.emailAddressIsEqualsTo;
import static org.assertj.core.api.Assertions.assertThat;

public class AccountSteps {

    private ConfigService configService = new InMemoryConfigService();
    private AuthenticationService authenticationService = new InMemoryAuthenticationService();
    private ClientRepository clientRepository = new ClientRepository(new InMemoryProvider<>());
    private AccountRepository accountRepository = new AccountRepository(new InMemoryProvider<>());
    private FixableClockService clockService = new FixableClockService();

    private Exception depositException;
    private Exception withdrawalException;

    private AccountService accountService = new AccountService(
            accountRepository,
            authenticationService,
            configService,
            clockService);
    private List<Operation> operations;


    @Given("^The minimum deposit amount is (\\d+) euros$")
    public void the_minimum_deposit_amount_is_euros(int minimumDepositAmount) throws Throwable {
        this.configService.setMinimumDepositAmount(minimumDepositAmount);
    }

    @Given("^A Client was subscribed with email address \"([^\"]*)\"$")
    public void a_Client_was_subscribed_with_email_address(String emailAddress) throws Throwable {
        this.clientRepository.put(
                new Client(new Client.Id(), emailAddress)
        );
    }

    @Given("^A new Account was created for Client \"([^\"]*)\"$")
    public void a_new_Account_was_created_for_Client(String emailAddress) throws Throwable {
        Client owner = this.clientRepository
                .findBy(emailAddressIsEqualsTo(emailAddress))
                .getInList().get(0);
        this.accountRepository.put(
                new Account(new Account.Id(), owner.getId())
        );
    }

    @Given("^I have logged as Client \"([^\"]*)\"$")
    public void i_have_logged_as_Client(String emailAddress) throws Throwable {
        Client client = this.clientRepository
                .findBy(emailAddressIsEqualsTo(emailAddress))
                .getInList().get(0);
        this.authenticationService.authenticate(client);
    }

    @When("^I deposit (\\d+) euros$")
    public void i_deposit_euros(int amountToDeposit) throws Throwable {
        try {
            this.accountService.deposit(amountToDeposit);
        } catch (Exception ex) {
            this.depositException = ex;
        }
    }

    @Then("^My balance is (\\d+) euros$")
    public void my_balance_is_euros(int expectedBalance) throws Throwable {
        assertThat(
                getAccountOfAuthenticatedClient().getBalance())
                .isEqualTo(expectedBalance);

    }

    @Then("^I have error : Cannot deposit less than the minimum deposit amount$")
    public void i_have_error_Cannot_deposit_less_than_the_minimum_deposit_amount() throws Throwable {
        assertThat(this.depositException).isNotNull();
        assertThat(this.depositException).isInstanceOf(DepositAmountLessThanTheMinimumException.class);
    }

    @Given("^I had deposited (\\d+) euros$")
    public void i_had_deposited_euros(int amount) throws Throwable {
        this.accountService.deposit(amount);
    }

    @When("^I withdrawal (\\d+) euros$")
    public void i_withdrawal_euros(int amount) {
        try {
            this.accountService.withdrawal(amount);
        } catch (InsufficientBalanceException e) {
            this.withdrawalException = e;
        }
    }

    @Then("^I have error : Insufficient balance$")
    public void i_have_error_Insufficient_balance() throws Throwable {
        assertThat(this.withdrawalException).isNotNull();
        assertThat(this.withdrawalException).isInstanceOf(InsufficientBalanceException.class);
    }

    @Given("^I had deposited (\\d+) euros on \"([^\"]*)\"$")
    public void i_had_deposited_euros_on(int amount, String date) throws Throwable {
        this.clockService.setFixedLocalDate(LocalDate.parse(date));
        this.accountService.deposit(amount);
        this.clockService.resetFixedLocalDate();
    }

    @Given("^I had withdrawn (\\d+) euros on \"([^\"]*)\"$")
    public void i_had_withdrawn_euros_on(int amount, String date) throws Throwable {
        this.clockService.setFixedLocalDate(LocalDate.parse(date));
        this.accountService.withdrawal(amount);
        this.clockService.resetFixedLocalDate();
    }

    @When("^I see my history$")
    public void i_see_my_history() throws Throwable {
        operations = this.accountService.getHistory();
    }

    @Then("^I have this list of operations :$")
    public void i_have_this_list_of_operations(DataTable table) throws Throwable {
        List<Map<String, String>> rows = table.asMaps(String.class, String.class);
        List<Operation> expectedOperations = new ArrayList<>();
        for (Map<String, String> columns : rows) {
            // | operation  | date       | amount | balance |
            expectedOperations.add(
                    new Operation(
                            Operation.Type.valueOf(columns.get("operation")),
                            LocalDate.parse(columns.get("date")),
                            Integer.parseInt(columns.get("amount")),
                            Integer.parseInt(columns.get("balance"))
                    )
            );
        }
        assertThat(this.operations).containsExactlyElementsOf(expectedOperations);
    }

    private Account getAccountOfAuthenticatedClient() {
        return this.accountRepository.findBy(AccountPredicates.ownerIdIsEqualsTo(
                this.authenticationService.getAuthenticatedClient().orElseThrow(
                        () -> new IllegalStateException("client should be authenticated")
                ).getId()
        )).getInList().get(0);
    }

}
