Feature: See the history
  In order to check my operations
  As a bank client
  I want to see the history (operation, date, amount, balance) of my operations

  Background:
    Given The minimum deposit amount is 10 euros
    Given A Client was subscribed with email address "abc@sg.com"
    Given A new Account was created for Client "abc@sg.com"
    Given I have logged as Client "abc@sg.com"

  Scenario: See the history (operation, date, amount, balance) of my operations
    Given I had deposited 10 euros on "2021-10-05"
    And I had deposited 20 euros on "2021-10-06"
    And I had withdrawn 15 euros on "2021-10-09"
    When I see my history
    Then I have this list of operations :
      | operation  | date       | amount | balance |
      | WITHDRAWAL | 2021-10-09 | 15     | 15      |
      | DEPOSIT    | 2021-10-06 | 20     | 30      |
      | DEPOSIT    | 2021-10-05 | 10     | 10      |
