package com.company.automation.core.validation;

import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Reusable response assertion utility.
 *
 * FIX: validateUserCount now uses "$" (root array) consistently,
 *      matching the actual jsonplaceholder response structure.
 * FIX: validateUserCount returns actual count so callers can use it
 *      for Allure attachments without parsing response twice.
 * NEW: validateResponseTime moved here from UserSteps for consistency.
 * NEW: validateNotEmpty, validateHeader for broader coverage.
 */
public class ResponseValidator {

    private static final Logger log = LoggerFactory.getLogger(ResponseValidator.class);

    /** Validates HTTP status code with descriptive failure message. */
    public static void validateStatusCode(Response response, int expectedStatus) {
        assertNotNull(response, "Response must not be null");
        log.debug("Validating status code — expected: {}, actual: {}",
                expectedStatus, response.getStatusCode());
        assertEquals(
                expectedStatus,
                response.getStatusCode(),
                "HTTP status code mismatch. Expected " + expectedStatus
                        + " but got " + response.getStatusCode()
        );
    }

    /**
     * FIX: JSONPath now consistently uses "$" (root element) for array responses.
     * Returns actual count so caller can use it without re-parsing.
     */
    public static int validateUserCount(Response response, int expectedCount) {
        assertNotNull(response, "Response must not be null");
        // FIX: "$" correctly targets the root JSON array returned by /users
        List<?> users = response.jsonPath().getList("$");
        int actualCount = users.size();
        log.debug("Validating user count — expected: {}, actual: {}", expectedCount, actualCount);
        assertThat(actualCount)
                .as("User count mismatch in response")
                .isEqualTo(expectedCount);
        return actualCount;
    }

    /**
     * NEW: Moved from UserSteps for consistency — all assertions live in validators.
     */
    public static void validateResponseTime(Response response, long maxTimeMs) {
        long actualTime = response.getTime();
        log.debug("Validating response time — max: {}ms, actual: {}ms", maxTimeMs, actualTime);
        assertThat(actualTime)
                .as("Response time exceeded SLA. Expected < %d ms but was %d ms", maxTimeMs, actualTime)
                .isLessThan(maxTimeMs);
    }

    /** NEW: Validates response body is not empty. */
    public static void validateNotEmpty(Response response) {
        assertNotNull(response, "Response must not be null");
        assertThat(response.getBody().asString())
                .as("Response body must not be empty")
                .isNotBlank();
    }

    /** NEW: Validates a specific response header exists and has expected value. */
    public static void validateHeader(Response response, String headerName, String expectedValue) {
        String actual = response.getHeader(headerName);
        assertThat(actual)
                .as("Header '%s' mismatch", headerName)
                .containsIgnoringCase(expectedValue);
    }
}