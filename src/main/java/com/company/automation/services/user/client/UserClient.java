package com.company.automation.services.user.client;

import com.company.automation.context.ScenarioContext;
import com.company.automation.core.client.BaseApiClient;
import com.company.automation.services.user.endpoints.UserEndpoints;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.company.automation.services.user.model.UserResponse;

/**
 * API client for all User service operations.
 *
 * FIX: Now uses UserEndpoints constants — no more hardcoded strings.
 * NEW: Added getUser(), createUser(), updateUser(), deleteUser() methods.
 */
public class UserClient extends BaseApiClient {

    private static final Logger log = LoggerFactory.getLogger(UserClient.class);

    private final ScenarioContext scenarioContext;

    public UserClient(ScenarioContext scenarioContext) {
        this.scenarioContext = scenarioContext;
    }

    /** GET /users */
    public Response getUsers() {
        log.info("Fetching all users");
        scenarioContext.setRequestMethod("GET");
        scenarioContext.setEndpoint(UserEndpoints.GET_USERS);
        // FIX: Use UserEndpoints constant instead of hardcoded "/users"
        Response response = get(UserEndpoints.GET_USERS);
        scenarioContext.setResponse(response);
        scenarioContext.setResponseTime(response.getTime());
        return response;
    }

    /** GET /users/{id} — NEW */
    public Response getUserById(int userId) {
        log.info("Fetching user by id: {}", userId);
        String endpoint = UserEndpoints.GET_USERS + "/" + userId;
        scenarioContext.setRequestMethod("GET");
        scenarioContext.setEndpoint(endpoint);
        Response response = getById(UserEndpoints.GET_USERS, userId);
        scenarioContext.setResponse(response);
        scenarioContext.setResponseTime(response.getTime());
        return response;
    }

    /** POST /users — NEW */
    public Response createUser(Object requestBody) {
        log.info("Creating user");
        scenarioContext.setRequestMethod("POST");
        scenarioContext.setEndpoint(UserEndpoints.CREATE_USER);
        scenarioContext.setRequestBody(requestBody);
        Response response = post(UserEndpoints.CREATE_USER, requestBody);
        scenarioContext.setResponse(response);
        scenarioContext.setResponseTime(response.getTime());
        return response;
    }

    /**
     * Typed response version of createUser
     */
    public UserResponse createUserTyped(Object requestBody) {

        log.info("Creating user (typed response)");

        Response response = createUser(requestBody);

        return response.as(UserResponse.class);
    }

    /** PUT /users/{id} — NEW */
    public Response updateUser(int userId, Object requestBody) {
        log.info("Updating user id: {}", userId);
        String endpoint = UserEndpoints.UPDATE_USER.replace("{id}", String.valueOf(userId));
        scenarioContext.setRequestMethod("PUT");
        scenarioContext.setEndpoint(endpoint);
        scenarioContext.setRequestBody(requestBody);
        Response response = put(endpoint, requestBody);
        scenarioContext.setResponse(response);
        scenarioContext.setResponseTime(response.getTime());
        return response;
    }

    /** DELETE /users/{id} — NEW */
    public Response deleteUser(int userId) {
        log.info("Deleting user id: {}", userId);
        String endpoint = UserEndpoints.DELETE_USER.replace("{id}", String.valueOf(userId));
        scenarioContext.setRequestMethod("DELETE");
        scenarioContext.setEndpoint(endpoint);
        Response response = delete(endpoint);
        scenarioContext.setResponse(response);
        scenarioContext.setResponseTime(response.getTime());
        return response;
    }
}