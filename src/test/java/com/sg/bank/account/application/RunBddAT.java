package com.sg.bank.account.application;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.Ignore;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        format = {
                "json:target/cucumber/bank-account.json",
                "html:target/cucumber/bank-account.html",
                "pretty"
        }//,
        //tags = {"~@ignored"}
)
public class RunBddAT {

}