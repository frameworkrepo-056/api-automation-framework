package com.company.automation.context;

import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

public class ScenarioContext {

    private Response response;
//    private long responseTime;
    private String requestMethod;
    private String endpoint;
    private Object requestBody;
    private static final ThreadLocal<Response> responseThreadLocal = new ThreadLocal<>();
    private static final ThreadLocal<Map<String, Object>> context =
            ThreadLocal.withInitial(HashMap::new);

    public static void setResponse(Response response) {
        responseThreadLocal.set(response);
    }

    public static Response getResponse() {
        return responseThreadLocal.get();
    }

    public static void set(String key, Object value) {
        context.get().put(key, value);
    }

    public static Object get(String key) {
        return context.get().get(key);
    }

    public static void clear() {
        responseThreadLocal.remove();
        context.remove();
    }

    private long responseTime;

    public void setResponseTime(long responseTime) {
        this.responseTime = responseTime;
    }

    public long getResponseTime() {
        return responseTime;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public Object getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(Object requestBody) {
        this.requestBody = requestBody;
    }
}