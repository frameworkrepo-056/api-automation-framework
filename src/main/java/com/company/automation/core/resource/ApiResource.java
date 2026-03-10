package com.company.automation.core.resource;

import com.company.automation.context.ScenarioContext;
import com.company.automation.core.client.BaseApiClient;
import io.restassured.response.Response;

import java.util.Arrays;
import java.util.List;

public abstract class ApiResource<TRequest, TResponse> extends BaseApiClient {

    private final String endpoint;
    private final Class<TResponse> responseType;
    private final Class<TResponse[]> responseArrayType;
    private final ScenarioContext scenarioContext;

    protected ApiResource(String endpoint,
                          Class<TResponse> responseType,
                          Class<TResponse[]> responseArrayType,
                          ScenarioContext scenarioContext) {

        this.endpoint = endpoint;
        this.responseType = responseType;
        this.responseArrayType = responseArrayType;
        this.scenarioContext = scenarioContext;
    }

    /* =============================
       CREATE
     ============================= */

    public TResponse create(TRequest request) {

        // NEW: capture request metadata
        scenarioContext.setRequestMethod("POST");
        scenarioContext.setEndpoint(endpoint);
        scenarioContext.setRequestBody(request);

        Response response = post(endpoint, request);

        updateContext(response);

        return response.as(responseType);
    }

    /* =============================
       GET BY ID
     ============================= */

    public TResponse getById(int id) {

        String url = endpoint + "/" + id;

        // NEW: capture request metadata
        scenarioContext.setRequestMethod("GET");
        scenarioContext.setEndpoint(url);

        Response response = get(url);

        updateContext(response);

        return response.as(responseType);
    }

    /* =============================
       GET ALL
     ============================= */

    public List<TResponse> getAll() {

        // NEW: capture request metadata
        scenarioContext.setRequestMethod("GET");
        scenarioContext.setEndpoint(endpoint);

        Response response = get(endpoint);

        updateContext(response);

        return Arrays.asList(response.as(responseArrayType));
    }

    /* =============================
       UPDATE
     ============================= */

    public TResponse update(int id, TRequest request) {

        String url = endpoint + "/" + id;

        // NEW: capture request metadata
        scenarioContext.setRequestMethod("PUT");
        scenarioContext.setEndpoint(url);
        scenarioContext.setRequestBody(request);

        Response response = put(url, request);

        updateContext(response);

        return response.as(responseType);
    }

    /* =============================
       DELETE
     ============================= */

    public Response deleteById(int id) {

        String url = endpoint + "/" + id;

        // NEW: capture request metadata
        scenarioContext.setRequestMethod("DELETE");
        scenarioContext.setEndpoint(url);

        Response response = delete(url);

        updateContext(response);

        return response;
    }

    /* =============================
       CONTEXT UPDATE
     ============================= */

    private void updateContext(Response response) {

        scenarioContext.setResponse(response);
        scenarioContext.setResponseTime(response.getTime());
    }
}