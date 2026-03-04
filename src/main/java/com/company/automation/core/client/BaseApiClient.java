package com.company.automation.core.client;

import com.company.automation.core.reporting.AllureAttachmentUtil;
import com.company.automation.core.spec.RequestSpecFactory;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static io.restassured.RestAssured.given;

/**
 * Base HTTP client providing GET, POST, PUT, PATCH, DELETE.
 * Every call automatically attaches request + response details to Allure report.
 * All subclass service clients (UserClient, OrderClient, etc.) extend this.
 */
public class BaseApiClient {

    private static final Logger log = LoggerFactory.getLogger(BaseApiClient.class);

    // ── GET ───────────────────────────────────────────────────────────────────

    protected Response get(String endpoint) {
        log.info("GET {}", endpoint);
        AllureAttachmentUtil.attachRequest("GET", endpoint, null);
        Response response = given()
                .spec(RequestSpecFactory.getRequestSpec())
                .when()
                .get(endpoint);
        AllureAttachmentUtil.attachResponse(response);
        log.debug("GET {} → {}", endpoint, response.getStatusCode());
        return response;
    }

    // NEW: GET with query parameters
    protected Response get(String endpoint, Map<String, Object> queryParams) {
        log.info("GET {} params={}", endpoint, queryParams);
        AllureAttachmentUtil.attachRequest("GET", endpoint + " params=" + queryParams, null);
        Response response = given()
                .spec(RequestSpecFactory.getRequestSpec())
                .queryParams(queryParams)
                .when()
                .get(endpoint);
        AllureAttachmentUtil.attachResponse(response);
        log.debug("GET {} → {}", endpoint, response.getStatusCode());
        return response;
    }

    // NEW: GET with path parameter
    protected Response getById(String endpoint, Object id) {
        String fullPath = endpoint + "/" + id;
        log.info("GET {}", fullPath);
        AllureAttachmentUtil.attachRequest("GET", fullPath, null);
        Response response = given()
                .spec(RequestSpecFactory.getRequestSpec())
                .when()
                .get(fullPath);
        AllureAttachmentUtil.attachResponse(response);
        log.debug("GET {} → {}", fullPath, response.getStatusCode());
        return response;
    }

    // ── POST ──────────────────────────────────────────────────────────────────

    protected Response post(String endpoint, Object body) {
        log.info("POST {}", endpoint);
        AllureAttachmentUtil.attachRequest("POST", endpoint, body);
        Response response = given()
                .spec(RequestSpecFactory.getRequestSpec())
                .body(body)
                .when()
                .post(endpoint);
        AllureAttachmentUtil.attachResponse(response);
        log.debug("POST {} → {}", endpoint, response.getStatusCode());
        return response;
    }

    // ── PUT ───────────────────────────────────────────────────────────────────

    // NEW
    protected Response put(String endpoint, Object body) {
        log.info("PUT {}", endpoint);
        AllureAttachmentUtil.attachRequest("PUT", endpoint, body);
        Response response = given()
                .spec(RequestSpecFactory.getRequestSpec())
                .body(body)
                .when()
                .put(endpoint);
        AllureAttachmentUtil.attachResponse(response);
        log.debug("PUT {} → {}", endpoint, response.getStatusCode());
        return response;
    }

    // ── PATCH ─────────────────────────────────────────────────────────────────

    // NEW
    protected Response patch(String endpoint, Object body) {
        log.info("PATCH {}", endpoint);
        AllureAttachmentUtil.attachRequest("PATCH", endpoint, body);
        Response response = given()
                .spec(RequestSpecFactory.getRequestSpec())
                .body(body)
                .when()
                .patch(endpoint);
        AllureAttachmentUtil.attachResponse(response);
        log.debug("PATCH {} → {}", endpoint, response.getStatusCode());
        return response;
    }

    // ── DELETE ────────────────────────────────────────────────────────────────

    // NEW
    protected Response delete(String endpoint) {
        log.info("DELETE {}", endpoint);
        AllureAttachmentUtil.attachRequest("DELETE", endpoint, null);
        Response response = given()
                .spec(RequestSpecFactory.getRequestSpec())
                .when()
                .delete(endpoint);
        AllureAttachmentUtil.attachResponse(response);
        log.debug("DELETE {} → {}", endpoint, response.getStatusCode());
        return response;
    }
}