package com.company.automation.core.auth;

import io.restassured.specification.RequestSpecification;

public class BearerTokenAuth implements AuthStrategy {

    private final String token;

    public BearerTokenAuth(String token) {
        this.token = token;
    }

    @Override
    public void apply(RequestSpecification requestSpec) {
        requestSpec.header("Authorization", "Bearer " + token);
    }
}