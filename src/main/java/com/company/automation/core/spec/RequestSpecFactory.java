package com.company.automation.core.spec;

import com.company.automation.core.config.ConfigReader;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

public class RequestSpecFactory {

    private static final ThreadLocal<RequestSpecification> requestSpec = new ThreadLocal<>();

    public static void init() {
        RequestSpecBuilder builder = new RequestSpecBuilder();
        builder.setBaseUri(ConfigReader.get("qa.base.url"));
        builder.setContentType("application/json");
        requestSpec.set(builder.build());
    }
    public static RequestSpecification getRequestSpec() {
        return requestSpec.get();
    }
}