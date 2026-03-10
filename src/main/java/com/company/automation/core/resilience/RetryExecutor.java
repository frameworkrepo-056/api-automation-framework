package com.company.automation.core.resilience;

import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.function.Supplier;

/**
 * Production-grade retry executor for transient API failures.
 *
 * Handles:
 * - Network glitches
 * - Temporary API gateway failures
 * - Rate limiting
 *
 * Retries only for safe HTTP status codes.
 */
public class RetryExecutor {

    private static final Logger log = LoggerFactory.getLogger(RetryExecutor.class);

    private static final int MAX_ATTEMPTS = 3;
    private static final long RETRY_DELAY_MS = 1000;

    /**
     * HTTP codes that are safe to retry.
     */
    private static final Set<Integer> RETRYABLE_STATUS_CODES = Set.of(
            429, // Too many requests
            500, // Internal server error
            502, // Bad gateway
            503, // Service unavailable
            504  // Gateway timeout
    );

    private RetryExecutor() {}

    public static Response execute(Supplier<Response> action) {

        int attempt = 1;

        while (true) {

            Response response = null;

            try {
                response = action.get();

                if (!shouldRetry(response)) {
                    return response;
                }

                log.warn(
                        "Retryable status {} detected (attempt {}/{})",
                        response.getStatusCode(),
                        attempt,
                        MAX_ATTEMPTS
                );

            } catch (Exception ex) {

                log.warn(
                        "Exception during API call (attempt {}/{}): {}",
                        attempt,
                        MAX_ATTEMPTS,
                        ex.getMessage()
                );

                if (attempt >= MAX_ATTEMPTS) {
                    throw ex;
                }
            }

            if (attempt >= MAX_ATTEMPTS) {
                return response;
            }

            sleep(RETRY_DELAY_MS);

            attempt++;
        }
    }

    private static boolean shouldRetry(Response response) {

        if (response == null) {
            return true;
        }

        return RETRYABLE_STATUS_CODES.contains(response.getStatusCode());
    }

    private static void sleep(long delay) {

        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}