Feature: Make a deposit
  In order to save money
  As a bank client
  I want to make a deposit in my account

  Rule: Cannot deposit less than the minimum deposit amount

    Background:
      Given The minimum deposit amount is 10 euros
      Given A Client was subscribed with email address "abc@sg.com"
      Given A new Account was created for Client "abc@sg.com"
      Given I have logged as Client "abc@sg.com"

    Scenario: Deposit more than the minimum deposit amount
      When I deposit 20 euros
      Then My balance is 20 euros

    Scenario: Deposit less than the minimum deposit amount
      When I deposit 5 euros
      Then I have error : Cannot deposit less than the minimum deposit amount
