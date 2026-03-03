package com.company.automation.core.reporting;

import io.qameta.allure.Allure;
import io.restassured.response.Response;

public class AllureAttachmentUtil {

    public static void attachRequest(String method, String endpoint, Object body) {

        String request = "METHOD: " + method + "\n" +
                "ENDPOINT: " + endpoint + "\n\n" +
                "BODY:\n" + (body != null ? body.toString() : "N/A");

        Allure.addAttachment("API Request", "text/plain", request);
    }

    public static void attachResponse(Response response) {

        Allure.addAttachment(
                "API Response",
                "application/json",
                response.getBody().asPrettyString(),
                ".json"
        );

        Allure.addAttachment(
                "Response Details",
                "text/plain",
                "Status Code: " + response.getStatusCode() +
                        "\nResponse Time: " + response.getTime() + " ms"
        );
    }
}