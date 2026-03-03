package com.company.automation.hooks;

import com.company.automation.context.ScenarioContext;
import com.company.automation.core.spec.RequestSpecFactory;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

public class ApiHooks {

    private final ScenarioContext scenarioContext;

    public ApiHooks(ScenarioContext scenarioContext) {
        this.scenarioContext = scenarioContext;
    }

    @Before
    public void beforeScenario() {

        // ✅ Initialize Request Specification for this thread
        RequestSpecFactory.init();

        System.out.println("========== SCENARIO START ==========");
    }

    @After
    public void afterScenario(Scenario scenario) {

        if (scenarioContext.getResponse() != null) {

            System.out.println("===== API TRACE =====");

            System.out.println("REQUEST:");
            System.out.println("Method  : " + scenarioContext.getRequestMethod());
            System.out.println("Endpoint: " + scenarioContext.getEndpoint());

            if (scenarioContext.getRequestBody() != null) {
                System.out.println("Body:\n" + scenarioContext.getRequestBody());
            }

            System.out.println("\nRESPONSE:");
            System.out.println("Status Code  : " + scenarioContext.getResponse().getStatusCode());
            System.out.println("Response Time: " + scenarioContext.getResponseTime() + " ms");
            System.out.println("Body:\n" + scenarioContext.getResponse().getBody().asPrettyString());

            System.out.println("=====================\n");
        }
    }
}