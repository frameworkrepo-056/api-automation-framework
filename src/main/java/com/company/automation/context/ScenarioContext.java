package com.company.automation.context;

import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

/**
 * Per-scenario state container, managed by PicoContainer.
 * PicoContainer creates one fresh instance per scenario — no ThreadLocal needed.
 * All fields are plain instance fields: safe, simple, and testable.
 *
 * FIX: Removed broken mixed static/ThreadLocal + instance field design.
 * FIX: Removed duplicate responseTime field (was declared twice).
 * FIX: Removed static methods — everything is now instance-based.
 * FIX: clear() now correctly resets all state for reuse safety.
 */
public class ScenarioContext {

    private Response response;
    private long responseTime;
    private String requestMethod;
    private String endpoint;
    private Object requestBody;

    // Generic key-value store for sharing arbitrary data between step definitions
    private final Map<String, Object> context = new HashMap<>();

    // ── Response ──────────────────────────────────────────────────────────────

    public void setResponse(Response response) {
        this.response = response;
    }

    public Response getResponse() {
        return response;
    }

    // ── Response Time ─────────────────────────────────────────────────────────

    public void setResponseTime(long responseTime) {
        this.responseTime = responseTime;
    }

    public long getResponseTime() {
        return responseTime;
    }

    // ── Request Metadata ──────────────────────────────────────────────────────

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

    // ── Generic Context Store ─────────────────────────────────────────────────

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

    // ── Lifecycle ─────────────────────────────────────────────────────────────

    /**
     * Called from @After hook to reset all state between scenarios.
     * Critical for long test runs to prevent state bleed.
     */
    public void clear() {
        response = null;
        responseTime = 0L;
        requestMethod = null;
        endpoint = null;
        requestBody = null;
        context.clear();
    }
}