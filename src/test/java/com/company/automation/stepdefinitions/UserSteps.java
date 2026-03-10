package com.company.automation.stepdefinitions;

import com.company.automation.context.ScenarioContext;
import com.company.automation.core.assertions.AssertionFactory;
import com.company.automation.core.validation.ResponseValidator;
import com.company.automation.core.validation.SchemaValidator;
import com.company.automation.services.user.client.UserClient;
import com.company.automation.services.user.model.UserRequest;
import com.company.automation.services.user.resource.UserResource;
import com.company.automation.testdata.user.UserDataFactory;
import com.company.automation.services.user.model.UserRequest;
import com.company.automation.services.user.model.UserResponse;
import com.company.automation.testdata.user.UserDataFactory;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.qameta.allure.Allure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * FIX: Removed stacked @Then + @And annotations from every method.
 *
 * In Cucumber, @Given/@Then/@When/@And/@But all register the step expression
 * against the SAME step definition registry. Putting two annotations on one
 * method registers the SAME regex twice → "Duplicate step definitions" error.
 *
 * Rule: use ONE annotation per method. Use @And only when you want a step
 * that is exclusively written as "And ..." in feature files (rarely needed —
 * Cucumber matches "And" to the nearest @Given/@Then/@When anyway).
 */
public class UserSteps {

    private static final Logger log = LoggerFactory.getLogger(UserSteps.class);

    private final ScenarioContext scenarioContext;
    private final UserClient userClient;

    public UserSteps(ScenarioContext scenarioContext) {
        this.scenarioContext = scenarioContext;
        this.userClient = new UserClient(scenarioContext);
    }

    // ── Given ─────────────────────────────────────────────────────────────────

    @Given("I call get users API")
    public void callUsersApi() {
        log.info("Step: Calling GET /users");
        var response = userClient.getUsers();
        Allure.addAttachment("Response Time", response.getTime() + " ms");
    }


    @Given("I create a random user")
    public void createRandomUser() {

        log.info("Step: Creating random user");

        UserRequest user = UserDataFactory.createRandomUser();

        var response = userClient.createUser(user);

        scenarioContext.setResponse(response);

        Allure.addAttachment("Create User Response Time", response.getTime() + " ms");
    }

    // ── Then ──────────────────────────────────────────────────────────────────
    // NOTE: In feature files you can freely write "Then", "And", "But" before
    // any of these — Cucumber resolves them all to @Then-annotated methods.
    // No need for a separate @And annotation.

    @Then("the response status should be {int}")
    public void verifyStatus(int status) {
        log.debug("Step: Verify status = {}", status);
        ResponseValidator.validateStatusCode(scenarioContext.getResponse(), status);
    }

    @Then("the response should contain {int} users")
    public void verifyUserCount(int count) {
        log.debug("Step: Verify user count = {}", count);
        int actualCount = ResponseValidator.validateUserCount(
                scenarioContext.getResponse(), count);
        Allure.addAttachment("Actual User Count", String.valueOf(actualCount));
    }

    @Then("the response should match {string} schema")
    public void validateSchema(String schemaFile) {
        log.debug("Step: Validate schema = {}", schemaFile);
        SchemaValidator.validateSchema(
                scenarioContext.getResponse(),
                "schemas/user/" + schemaFile
        );
    }

    @Then("the response time should be less than {int} ms")
    public void validateResponseTime(int maxTime) {
        log.debug("Step: Validate response time < {}ms", maxTime);
        ResponseValidator.validateResponseTime(scenarioContext.getResponse(), maxTime);
        Allure.addAttachment(
                "Response Time Validation",
                "Max allowed: " + maxTime + " ms\nActual: "
                        + scenarioContext.getResponseTime() + " ms"
        );
    }

    @Then("the response body should not be empty")
    public void validateNotEmpty() {
        log.debug("Step: Validate response body not empty");
        ResponseValidator.validateNotEmpty(scenarioContext.getResponse());
    }


    @When("I create a user using resource layer")
    public void createUserUsingResource() {

        UserResource userResource = new UserResource(scenarioContext);

        UserRequest user = UserDataFactory.createRandomUser();

        UserResponse response = userResource.create(user);

        log.info("User created with id: {}", response.getId());
    }

    @When("I create a random user using resource")
    public void createRandomUserUsingResource() {

        log.info("Step: Creating random user using Resource Pattern");

        UserResource userResource = new UserResource(scenarioContext);

        UserRequest user = UserDataFactory.createRandomUser();

        UserResponse response = userResource.create(user);

        log.info("User created with id: {}", response.getId());
    }

    @Then("the API response should be valid for user creation")
    public void validateUserCreationResponse() {

        AssertionFactory
                .apiAssert(scenarioContext)
                .status(201)
                .responseTimeBelow(3000)
                .notEmpty();
    }


}