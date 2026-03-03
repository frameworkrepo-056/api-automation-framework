package com.company.automation.core.client;

import com.company.automation.core.reporting.AllureAttachmentUtil;
import com.company.automation.core.spec.RequestSpecFactory;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class BaseApiClient {

    protected Response get(String endpoint) {

        AllureAttachmentUtil.attachRequest("GET", endpoint, null);

        Response response = given()
                .spec(RequestSpecFactory.getRequestSpec())
                .when()
                .get(endpoint);

        AllureAttachmentUtil.attachResponse(response);

        return response;
    }

    protected Response post(String endpoint, Object body) {

        AllureAttachmentUtil.attachRequest("POST", endpoint, body);

        Response response = given()
                .spec(RequestSpecFactory.getRequestSpec())
                .body(body)
                .when()
                .post(endpoint);

        AllureAttachmentUtil.attachResponse(response);

        return response;
    }
}