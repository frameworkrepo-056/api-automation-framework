package com.company.automation.hooks;

import com.company.automation.context.ScenarioContext;
import com.company.automation.core.reporting.AllureAttachmentUtil;
import com.company.automation.core.spec.RequestSpecFactory;
import com.company.automation.utils.AllureEnvironmentWriter;
import com.company.automation.utils.JsonUtil;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApiHooks {

    private static final Logger log = LoggerFactory.getLogger(ApiHooks.class);

    private final ScenarioContext scenarioContext;

    public ApiHooks(ScenarioContext scenarioContext) {
        this.scenarioContext = scenarioContext;
    }

    /**
     * FIX: Accept Scenario param to log scenario name.
     * Initialise RequestSpec fresh for each scenario (thread-safe via ThreadLocal).
     */
    @Before(order = 0)
    public void beforeScenario(Scenario scenario) {
        log.info("▶ START — [{}] {}", scenario.getId(), scenario.getName());
        log.debug("Tags: {}", scenario.getSourceTagNames());
        RequestSpecFactory.init();
    }

//    @Before(order = 1)
//    public void attachEnvironment() {
//        AllureEnvironmentWriter.writeEnvironmentInfo();
//    }
    /**
     * FIX: Call scenarioContext.clear() to prevent state bleed between scenarios.
     * FIX: Call RequestSpecFactory.clear() to clean up ThreadLocal memory.
     * FIX: Replace System.out.println with SLF4J logging.
     * NEW: Attach failure context to Allure only when scenario fails.
     */
    @After(order = 0)
    public void afterScenario(Scenario scenario) {

        // ------------------------------------------------------------------
        // Attach failure context in Allure for failed scenarios
        // ------------------------------------------------------------------
        if (scenario.isFailed() && scenarioContext.getResponse() != null) {

            log.warn("✗ FAILED — {}", scenario.getName());

            // Attach failure details including response body and metadata
            AllureAttachmentUtil.attachFailureContext(
                    scenario.getName(),
                    scenarioContext.getResponse()
            );

        } else {
            log.info("✔ PASSED — {}", scenario.getName());
        }

        // ------------------------------------------------------------------
        // DEBUG: Print full API trace for troubleshooting
        // Visible only when log level = DEBUG
        // ------------------------------------------------------------------
        if (scenarioContext.getResponse() != null) {

            log.debug("===== API TRACE =====");

            log.debug("Method   : {}", scenarioContext.getRequestMethod());
            log.debug("Endpoint : {}", scenarioContext.getEndpoint());

            if (scenarioContext.getRequestBody() != null) {
                log.debug("Body     : {}", JsonUtil.toJson(scenarioContext.getRequestBody()));
            }

            log.debug("Status   : {}", scenarioContext.getResponse().getStatusCode());
            log.debug("Time     : {} ms", scenarioContext.getResponseTime());

            log.debug("=====================");
        }

        // ------------------------------------------------------------------
        // IMPORTANT: Clear scenario context to prevent data bleed
        // between test scenarios
        // ------------------------------------------------------------------
        scenarioContext.clear();

        // ------------------------------------------------------------------
        // Clear ThreadLocal request specification
        // Prevents memory leaks when running parallel tests
        // ------------------------------------------------------------------
        RequestSpecFactory.clear();

        // ------------------------------------------------------------------
        // Final scenario log
        // ------------------------------------------------------------------
        log.info("◀ END — {}", scenario.getName());
    }
}