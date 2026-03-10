package com.company.automation.core.spec;

import com.company.automation.core.config.ConfigReader;
import com.company.automation.core.logging.ApiLoggingFilter;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Thread-safe RequestSpecification factory.
 * Uses ThreadLocal so each parallel thread gets its own isolated spec.
 * Initialised once per scenario via @Before hook.
 */
public class RequestSpecFactory {

    private static final Logger log = LoggerFactory.getLogger(RequestSpecFactory.class);
    private static final ThreadLocal<RequestSpecification> requestSpec = new ThreadLocal<>();

    private static final ApiLoggingFilter API_LOGGING_FILTER = new ApiLoggingFilter();


    public static void init() {
        String baseUrl = ConfigReader.getBaseUrl();
        log.info("Initialising RequestSpec — Base URL: {}", baseUrl);

        RequestSpecBuilder builder = new RequestSpecBuilder()
                .setBaseUri(baseUrl)
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .addFilter(API_LOGGING_FILTER)   // NEW
                // NEW: Log request details to SLF4J at DEBUG level
                .log(LogDetail.URI);

        // NEW: Optional auth token support — set via config or system property
        String authToken = ConfigReader.get("auth.token");
        if (authToken != null && !authToken.isBlank()) {
            builder.addHeader("Authorization", "Bearer " + authToken);
            log.debug("Auth token applied to RequestSpec");
        }

        requestSpec.set(builder.build());
    }

    public static RequestSpecification getRequestSpec() {
        RequestSpecification spec = requestSpec.get();
        if (spec == null) {
            throw new IllegalStateException(
                    "RequestSpec not initialised. Ensure @Before hook calls RequestSpecFactory.init()");
        }
        return spec;
    }

    /**
     * FIX: Called in @After to clean up ThreadLocal and prevent memory leaks.
     */
    public static void clear() {
        requestSpec.remove();
    }
}