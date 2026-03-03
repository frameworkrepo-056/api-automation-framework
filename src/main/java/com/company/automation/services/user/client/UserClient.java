package com.company.automation.services.user.client;

import com.company.automation.context.ScenarioContext;
import com.company.automation.core.client.BaseApiClient;
import io.restassured.response.Response;
import com.company.automation.services.user.endpoints.UserEndpoints;

public class UserClient extends BaseApiClient {
    private final ScenarioContext scenarioContext;

    public UserClient(ScenarioContext scenarioContext) {
        // DO NOT call super(scenarioContext)
        this.scenarioContext = scenarioContext;
    }

    public Response getUsers() {
        Response response = get("/users");
        scenarioContext.setResponse(response);
        scenarioContext.setResponseTime(response.getTime());
        return response;
    }
}