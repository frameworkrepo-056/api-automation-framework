package com.company.automation.core.auth;

import io.restassured.specification.RequestSpecification;

public class OAuth2Auth implements AuthStrategy {

    private final String accessToken;

    public OAuth2Auth(String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public void apply(RequestSpecification requestSpec) {
        requestSpec.auth().oauth2(accessToken);
    }
}