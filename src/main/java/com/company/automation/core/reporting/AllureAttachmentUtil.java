package com.company.automation.core.reporting;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.Allure;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * -----------------------------------------------------------------------------
 * AllureAttachmentUtil
 * -----------------------------------------------------------------------------
 *
 * Centralized utility responsible for attaching API execution details
 * into Allure reports.
 *
 * This class is intentionally designed as a STATIC utility so that it can be
 * called easily from core framework layers such as:
 *
 *      BaseApiClient
 *      API filters
 *      Hooks (for failure context)
 *
 * Why this class exists:
 *
 * 1️⃣ Provide rich debugging information in Allure reports
 * 2️⃣ Avoid duplicating attachment logic in step definitions
 * 3️⃣ Keep reporting logic separated from API execution logic
 *
 * Attachments produced by this utility:
 *
 * ✔ API Request details
 * ✔ API Response body
 * ✔ Response metadata (status code, response time)
 * ✔ cURL command (for developers to reproduce failures)
 * ✔ Failure context when scenarios fail
 *
 * Framework Tools Used:
 *
 * - REST Assured  → API execution
 * - Jackson       → POJO → JSON serialization
 * - Allure        → Test reporting
 *
 * -----------------------------------------------------------------------------
 */
public class AllureAttachmentUtil {

    private static final Logger log =
            LoggerFactory.getLogger(AllureAttachmentUtil.class);

    /**
     * Shared ObjectMapper instance.
     *
     * Using a singleton improves performance and avoids repeated
     * object allocation during test execution.
     */
    private static final ObjectMapper MAPPER = new ObjectMapper();


    /**
     * -------------------------------------------------------------------------
     * Attach API Request Details
     * -------------------------------------------------------------------------
     *
     * Called BEFORE the API request is executed.
     *
     * Captures:
     * - HTTP Method
     * - Endpoint
     * - Request body
     *
     * Also generates a cURL command to easily reproduce the API call.
     *
     * @param method   HTTP method (GET/POST/PUT/DELETE)
     * @param endpoint API endpoint path
     * @param body     Request body (POJO / String / null)
     */
    public static void attachRequest(String method, String endpoint, Object body) {

        String bodyStr = serializeBody(body);

        String requestDetails =
                "METHOD  : " + method + "\n" +
                        "ENDPOINT: " + endpoint + "\n\n" +
                        "BODY:\n" + bodyStr;

        /**
         * Attach request details.
         *
         * Using JSON content-type improves readability in Allure UI.
         */
        Allure.addAttachment(
                "📤 API Request",
                "application/json",
                requestDetails,
                ".json"
        );

        /**
         * Attach equivalent cURL command.
         *
         * Developers can directly copy and run this in terminal
         * to reproduce API calls during debugging.
         */
        attachCurlCommand(method, endpoint, bodyStr);
    }


    /**
     * -------------------------------------------------------------------------
     * Attach API Response Details
     * -------------------------------------------------------------------------
     *
     * Called AFTER API execution completes.
     *
     * Creates two attachments:
     *
     * 1️⃣ Response body (JSON formatted)
     * 2️⃣ Response metadata (status, response time, content-type)
     *
     * @param response REST Assured response object
     */
    public static void attachResponse(Response response) {

        if (response == null) {
            log.warn("Attempted to attach null response to Allure");
            return;
        }

        /**
         * Attach formatted JSON response body.
         */
        Allure.addAttachment(
                "📥 API Response Body",
                "application/json",
                response.getBody().asPrettyString(),
                ".json"
        );

        /**
         * Attach response metadata separately for quick inspection.
         */
        String meta =
                "Status Code  : " + response.getStatusCode() + "\n" +
                        "Response Time: " + response.getTime() + " ms\n" +
                        "Content-Type : " + response.getContentType();

        Allure.addAttachment(
                "📊 Response Details",
                "text/plain",
                meta
        );
    }


    /**
     * -------------------------------------------------------------------------
     * Attach Failure Context
     * -------------------------------------------------------------------------
     *
     * Should be called inside Cucumber @After hook when scenario fails.
     *
     * Provides a snapshot of the final API state when the test failed.
     *
     * @param scenarioName failing scenario name
     * @param response     last API response
     */
    public static void attachFailureContext(String scenarioName, Response response) {

        if (response == null) return;

        String failureInfo =
                "FAILED SCENARIO: " + scenarioName + "\n" +
                        "Status Code  : " + response.getStatusCode() + "\n" +
                        "Response Time: " + response.getTime() + " ms\n\n" +
                        "Response Body:\n" +
                        response.getBody().asPrettyString();

        Allure.addAttachment(
                "❌ Failure Context",
                "text/plain",
                failureInfo
        );
    }


    /**
     * -------------------------------------------------------------------------
     * Attach cURL Command
     * -------------------------------------------------------------------------
     *
     * Generates a curl command equivalent of the API request.
     *
     * This is extremely useful when debugging failures because
     * developers can directly execute the same request from terminal.
     */
    private static void attachCurlCommand(String method, String endpoint, String body) {

        try {

            String curl =
                    "curl -X " + method + " \"" + endpoint + "\" \\\n" +
                            "-H \"Content-Type: application/json\" \\\n" +
                            "-d '" + body + "'";

            Allure.addAttachment(
                    "🧪 cURL Command",
                    "text/plain",
                    curl
            );

        } catch (Exception e) {

            log.warn("Failed to generate curl command for Allure", e);
        }
    }


    /**
     * -------------------------------------------------------------------------
     * Serialize Request Body
     * -------------------------------------------------------------------------
     *
     * Converts request body into readable JSON.
     *
     * Supported inputs:
     *
     * null   → "N/A"
     * String → returned directly
     * POJO   → serialized using Jackson
     *
     * If serialization fails → fallback to toString()
     */
    private static String serializeBody(Object body) {

        if (body == null) {
            return "N/A (no request body)";
        }

        if (body instanceof String) {
            return (String) body;
        }

        try {

            return MAPPER
                    .writerWithDefaultPrettyPrinter()
                    .writeValueAsString(body);

        } catch (Exception e) {

            log.warn(
                    "Could not serialize request body to JSON. Falling back to toString()",
                    e
            );

            return body.toString();
        }
    }
}