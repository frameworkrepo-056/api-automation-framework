package com.company.automation.core.validation;

import io.restassured.response.Response;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ResponseValidator {

    public static void validateStatusCode(Response response, int expectedStatus) {
        assertNotNull(response, "Response is null");
        assertEquals(expectedStatus, response.getStatusCode(),
                "Status code mismatch");
    }

    public static void validateUserCount(Response response, int expectedCount) {
        assertNotNull(response, "Response is null");
        assertEquals(expectedCount,
                response.jsonPath().getList("$").size(),
                "User count mismatch");
    }
}