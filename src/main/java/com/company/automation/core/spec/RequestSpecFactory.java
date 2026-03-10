package com.company.automation.core.spec;

import com.company.automation.core.auth.AuthManager;
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
 *
 * Purpose:
 * - Creates base REST request configuration
 * - Ensures each parallel thread has its own RequestSpecification
 * - Applies logging filters
 * - Applies authentication automatically via AuthManager
 *
 * Thread safety:
 * Uses ThreadLocal so each scenario running in parallel gets an isolated spec.
 */
public class RequestSpecFactory {

    private static final Logger log = LoggerFactory.getLogger(RequestSpecFactory.class);

    /**
     * ThreadLocal storage for RequestSpecification.
     * Each parallel thread gets its own spec instance.
     */
    private static final ThreadLocal<RequestSpecification> requestSpec = new ThreadLocal<>();

    /**
     * Custom API logging filter for request/response logging.
     */
    private static final ApiLoggingFilter API_LOGGING_FILTER = new ApiLoggingFilter();


    /**
     * Initialise RequestSpecification for current scenario.
     * Called from Cucumber @Before hook.
     */
    public static void init() {

        String baseUrl = ConfigReader.getBaseUrl();
        log.info("Initialising RequestSpec — Base URL: {}", baseUrl);

        RequestSpecBuilder builder = new RequestSpecBuilder()
                .setBaseUri(baseUrl)
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .addFilter(API_LOGGING_FILTER)
                // Log request URI at DEBUG level
                .log(LogDetail.URI);

        RequestSpecification spec = builder.build();

        /**
         * Apply authentication dynamically based on config.
         *
         * Supported:
         * - Bearer tokens
         * - OAuth2
         * - API Keys
         * - JWT
         */
        AuthManager.applyAuth(spec);

        // Store spec for current thread
        requestSpec.set(spec);
    }


    /**
     * Returns thread-safe RequestSpecification.
     */
    public static RequestSpecification getRequestSpec() {

        RequestSpecification spec = requestSpec.get();

        if (spec == null) {
            throw new IllegalStateException(
                    "RequestSpec not initialised. Ensure @Before hook calls RequestSpecFactory.init()");
        }

        return spec;
    }


    /**
     * Cleanup ThreadLocal after scenario execution.
     * Prevents memory leaks during parallel execution.
     */
    public static void clear() {
        requestSpec.remove();
    }
}