package com.company.automation.core.reporting;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.Allure;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Centralised Allure attachment utility.
 * Called from BaseApiClient on every HTTP request/response.
 *
 * FIX: body.toString() replaced with Jackson serialization for POJOs.
 * NEW: attachFailureContext() for failure-only attachment in @After.
 */
public class AllureAttachmentUtil {

    private static final Logger log = LoggerFactory.getLogger(AllureAttachmentUtil.class);
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static void attachRequest(String method, String endpoint, Object body) {
        String bodyStr = serializeBody(body);
        String requestDetails = "METHOD  : " + method + "\n"
                + "ENDPOINT: " + endpoint + "\n\n"
                + "BODY:\n" + bodyStr;
        Allure.addAttachment("📤 API Request", "text/plain", requestDetails);
    }

    public static void attachResponse(Response response) {
        // Attach response body as formatted JSON
        Allure.addAttachment(
                "📥 API Response Body",
                "application/json",
                response.getBody().asPrettyString(),
                ".json"
        );
        // Attach response metadata separately
        String meta = "Status Code  : " + response.getStatusCode() + "\n"
                + "Response Time: " + response.getTime() + " ms\n"
                + "Content-Type : " + response.getContentType();
        Allure.addAttachment("📊 Response Details", "text/plain", meta);
    }

    /**
     * NEW: Call this only on scenario failure — adds failure-specific context.
     */
    public static void attachFailureContext(String scenarioName, Response response) {
        if (response == null) return;
        String failureInfo = "FAILED SCENARIO: " + scenarioName + "\n"
                + "Status Code  : " + response.getStatusCode() + "\n"
                + "Response Time: " + response.getTime() + " ms\n\n"
                + "Response Body:\n" + response.getBody().asPrettyString();
        Allure.addAttachment("❌ Failure Context", "text/plain", failureInfo);
    }

    // ── Private Helpers ───────────────────────────────────────────────────────

    private static String serializeBody(Object body) {
        if (body == null) return "N/A (no request body)";
        if (body instanceof String) return (String) body;
        try {
            // FIX: Use Jackson for proper POJO → JSON serialization
            return MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(body);
        } catch (Exception e) {
            log.warn("Could not serialize request body to JSON, falling back to toString()", e);
            return body.toString();
        }
    }
}