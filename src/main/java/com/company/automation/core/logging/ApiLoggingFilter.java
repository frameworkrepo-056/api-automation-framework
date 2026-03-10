package com.company.automation.core.logging;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Central logging filter for all API calls.
 * Automatically logs request and response details.
 */
public class ApiLoggingFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(ApiLoggingFilter.class);
    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public Response filter(FilterableRequestSpecification requestSpec,
                           FilterableResponseSpecification responseSpec,
                           FilterContext ctx) {

        log.info("➡ {} {}", requestSpec.getMethod(), requestSpec.getURI());

        if (requestSpec.getBody() != null) {
            log.debug("Request Body:\n{}", pretty(requestSpec.getBody()));
        }

        Response response = ctx.next(requestSpec, responseSpec);

        log.info("⬅ Status: {} | Time: {} ms",
                response.getStatusCode(),
                response.getTime());

        log.debug("Response Body:\n{}", response.getBody().asPrettyString());

        return response;
    }

    private String pretty(Object body) {
        if (body == null) return "N/A";
        try {
            return MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(body);
        } catch (Exception e) {
            return body.toString();
        }
    }
}