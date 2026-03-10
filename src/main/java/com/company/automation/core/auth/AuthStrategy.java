package com.company.automation.core.auth;

import io.restassured.specification.RequestSpecification;

/**
 * Authentication strategy interface.
 * Each auth mechanism implements this.
 */
public interface AuthStrategy {

    void apply(RequestSpecification requestSpec);
}