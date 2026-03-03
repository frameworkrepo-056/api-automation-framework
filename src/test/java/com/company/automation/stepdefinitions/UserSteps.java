package com.company.automation.stepdefinitions;

import com.company.automation.context.ScenarioContext;
import com.company.automation.core.validation.ResponseValidator;
import com.company.automation.core.validation.SchemaValidator;
import com.company.automation.services.user.client.UserClient;
import io.cucumber.java.en.*;
import io.qameta.allure.Step;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.Assertions;

public class UserSteps {

    private final ScenarioContext scenarioContext;
    private final UserClient userClient;

    // Constructor Injection
    public UserSteps(ScenarioContext scenarioContext) {
        this.scenarioContext = scenarioContext;
        this.userClient = new UserClient(scenarioContext);
    }

    @Given("I call get users API")
    @Step("Call Get Users API")
    public void callUsersApi() {

        var response = userClient.getUsers();

        scenarioContext.setResponse(response);
        scenarioContext.setResponseTime(response.getTime());

        Allure.addAttachment(
                "Captured Response Time",
                response.getTime() + " ms"
        );
    }

    @Then("the response status should be {int}")
    @Step("Validate response status is {0}")
    public void verifyStatus(int status) {

        ResponseValidator.validateStatusCode(
                scenarioContext.getResponse(),
                status
        );
    }

    @Then("the response should contain {int} users")
    @Step("Validate response contains {0} users")
    public void verifyUserCount(int count) {

        ResponseValidator.validateUserCount(
                scenarioContext.getResponse(),
                count
        );

        int actualCount = scenarioContext.getResponse()
                .jsonPath()
                .getList("data")
                .size();

        Allure.addAttachment(
                "Actual User Count",
                String.valueOf(actualCount)
        );
    }

    @Then("the response should match {string} schema")
    @Step("Validate response matches schema: {0}")
    public void validateSchema(String schemaFile) {

        SchemaValidator.validateSchema(
                scenarioContext.getResponse(),
                "schemas/user/" + schemaFile
        );
    }

    @Then("the response time should be less than {int} ms")
    @Step("Validate response time is less than {0} ms")
    public void validateResponseTime(int maxTime) {

        long actualTime = scenarioContext.getResponseTime();

        Allure.addAttachment(
                "Response Time Validation",
                "Expected < " + maxTime + " ms\nActual: " + actualTime + " ms"
        );

        Assertions.assertTrue(
                actualTime < maxTime,
                "Expected response time < " + maxTime + " ms but was " + actualTime + " ms"
        );
    }
}