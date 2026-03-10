package com.company.automation.core.auth;

import io.restassured.specification.RequestSpecification;

public class ApiKeyAuth implements AuthStrategy {

    private final String headerName;
    private final String apiKey;

    public ApiKeyAuth(String headerName, String apiKey) {
        this.headerName = headerName;
        this.apiKey = apiKey;
    }

    @Override
    public void apply(RequestSpecification requestSpec) {
        requestSpec.header(headerName, apiKey);
    }
}