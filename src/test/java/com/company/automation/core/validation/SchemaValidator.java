package com.company.automation.core.validation;

import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;

import static org.hamcrest.MatcherAssert.assertThat;

public class SchemaValidator {

    public static void validateSchema(Response response, String schemaPath) {
        assertThat(response.getBody().asString(),
                JsonSchemaValidator.matchesJsonSchemaInClasspath(schemaPath));
    }
}