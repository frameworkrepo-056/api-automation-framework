package com.company.automation.core.assertions;

import com.company.automation.context.ScenarioContext;
import com.company.automation.core.validation.ResponseValidator;
import com.company.automation.core.validation.SchemaValidator;
import io.restassured.response.Response;

public class ApiAssertions {

    private final ScenarioContext scenarioContext;

    public ApiAssertions(ScenarioContext scenarioContext) {
        this.scenarioContext = scenarioContext;
    }

    public ApiAssertions status(int expectedStatus) {

        Response response = scenarioContext.getResponse();
        ResponseValidator.validateStatusCode(response, expectedStatus);

        return this;
    }

    public ApiAssertions responseTimeBelow(long maxTime) {

        Response response = scenarioContext.getResponse();
        ResponseValidator.validateResponseTime(response, maxTime);

        return this;
    }

    public ApiAssertions schema(String schemaPath) {

        Response response = scenarioContext.getResponse();
        SchemaValidator.validateSchema(response, schemaPath);

        return this;
    }

    public ApiAssertions notEmpty() {

        Response response = scenarioContext.getResponse();
        ResponseValidator.validateResponseNotEmpty(response);

        return this;
    }
}