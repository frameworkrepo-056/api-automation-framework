package com.company.automation.context;

import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * -----------------------------------------------------------------------------
 * ScenarioContext
 * -----------------------------------------------------------------------------
 *
 * A per-scenario state container used to share data between step definitions,
 * hooks, and API client layers.
 *
 * Lifecycle:
 * - Managed automatically by PicoContainer.
 * - A new instance is created for EVERY Cucumber scenario.
 *
 * This design ensures:
 *
 * ✔ Thread safety during parallel execution
 * ✔ Clean isolation between scenarios
 * ✔ Simple dependency injection into step definitions
 *
 * Important Notes:
 *
 * - No static variables are used.
 * - No ThreadLocal is required because PicoContainer handles scoping.
 * - State is reset using clear() after scenario completion.
 *
 * Typical stored data:
 *
 * - REST Assured Response
 * - Response time
 * - Request metadata
 * - Request body
 * - Extracted response values (e.g., id)
 * - Arbitrary shared test data
 *
 * -----------------------------------------------------------------------------
 */
public class ScenarioContext {

    /**
     * Unique identifier for the scenario instance.
     *
     * Helpful when debugging logs during parallel execution.
     */
    private final String scenarioId = UUID.randomUUID().toString();

    private Response response;
    private long responseTime;

    private String requestMethod;
    private String endpoint;
    private Object requestBody;

    private Integer responseId;

    /**
     * Generic storage map used for sharing arbitrary values
     * between step definitions.
     *
     * Example usage:
     * context.set("userId", 101);
     * context.get("userId");
     */
    private final Map<String, Object> context = new HashMap<>();


    // -------------------------------------------------------------------------
    // Scenario Metadata
    // -------------------------------------------------------------------------

    public String getScenarioId() {
        return scenarioId;
    }


    // -------------------------------------------------------------------------
    // Response Handling
    // -------------------------------------------------------------------------

    public void setResponse(Response response) {
        this.response = response;
    }

    public Response getResponse() {
        return response;
    }


    // -------------------------------------------------------------------------
    // Response Time
    // -------------------------------------------------------------------------

    public void setResponseTime(long responseTime) {
        this.responseTime = responseTime;
    }

    public long getResponseTime() {
        return responseTime;
    }


    // -------------------------------------------------------------------------
    // Request Metadata
    // -------------------------------------------------------------------------

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setRequestBody(Object requestBody) {
        this.requestBody = requestBody;
    }

    public Object getRequestBody() {
        return requestBody;
    }


    // -------------------------------------------------------------------------
    // Extracted Response Data
    // -------------------------------------------------------------------------

    /**
     * Example: extracted ID from response.
     * Used across steps for validation or follow-up API calls.
     */
    public Integer getResponseId() {
        return responseId;
    }

    public void setResponseId(Integer responseId) {
        this.responseId = responseId;
    }


    // -------------------------------------------------------------------------
    // Generic Context Storage
    // -------------------------------------------------------------------------

    public void set(String key, Object value) {
        context.put(key, value);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        return (T) context.get(key);
    }

    public boolean has(String key) {
        return context.containsKey(key);
    }


    // -------------------------------------------------------------------------
    // Lifecycle Management
    // -------------------------------------------------------------------------

    /**
     * Clears scenario state.
     *
     * Should be called in Cucumber @After hook to prevent
     * state leakage between scenarios during long executions.
     */
    public void clear() {
        response = null;
        responseTime = 0L;
        requestMethod = null;
        endpoint = null;
        requestBody = null;
        responseId = null;
        context.clear();
    }
}