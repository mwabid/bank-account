Feature: Make a withdrawal
  In order to retrieve some or all of my savings
  As a bank client
  I want to make a withdrawal from my account

  Rule: Cannot withdrawal more than the balance

    Background:
      Given The minimum deposit amount is 10 euros
      Given A Client was subscribed with email address "abc@sg.com"
      Given A new Account was created for Client "abc@sg.com"
      Given I have logged as Client "abc@sg.com"

    Scenario: Sufficient balance
      Given I had deposited 30 euros
      When I withdrawal 20 euros
      Then My balance is 10 euros

    Scenario: Insufficient balance
      Given I had deposited 10 euros
      When I withdrawal 20 euros
      Then My balance is 10 euros
      And I have error : Insufficient balance
