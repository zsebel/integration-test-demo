package com.epam.bitcoin.integration.cucumber.steps;

import org.junit.jupiter.api.Assertions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class HelloWorldStepDefs {

    private String today;
    private String actualResponse;

    @Given("today is Sunday")
    public void setTodayToSunday() {
        today = "Sunday";
    }
    @When("I ask whether it's Friday yet")
    public void checkWhetherTodayIsFridayYet() {
        actualResponse = "Friday".equals(today) ? "Yes." : "Nope";
    }
    @Then("I should be told {string}")
    public void verifyResult(String expectedAnswer) {
        Assertions.assertEquals(expectedAnswer, actualResponse);
    }

}
