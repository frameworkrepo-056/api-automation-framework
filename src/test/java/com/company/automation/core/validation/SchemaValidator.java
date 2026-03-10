package com.company.automation.core.validation;

import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * -----------------------------------------------------------------------------
 * SchemaValidator
 * -----------------------------------------------------------------------------
 *
 * Responsible for validating API responses against JSON schemas.
 *
 * Why schema validation is important:
 *
 * 1️⃣ Ensures API contract integrity
 * 2️⃣ Detects breaking changes in API responses
 * 3️⃣ Guarantees required fields exist
 * 4️⃣ Validates response structure and data types
 *
 * This validator integrates with REST Assured's JSON Schema Validator
 * and can validate responses against schemas stored in the classpath.
 *
 * Schema files should be placed under:
 *
 * src/test/resources/schemas/
 *
 * Example folder structure:
 *
 * schemas/
 *   user/
 *     get_users_schema.json
 *
 * Example usage from Step Definition:
 *
 * SchemaValidator.validateSchema(response, "schemas/user/get_users_schema.json");
 *
 * Tools involved:
 * - REST Assured
 * - JSON Schema Draft-07
 *
 * -----------------------------------------------------------------------------
 */
public class SchemaValidator {

    private static final Logger log =
            LoggerFactory.getLogger(SchemaValidator.class);

    /**
     * -------------------------------------------------------------------------
     * Validate Response Against JSON Schema
     * -------------------------------------------------------------------------
     *
     * This method validates the entire API response body against
     * the provided JSON schema file.
     *
     * Validation includes:
     *  - Required fields
     *  - Data types
     *  - Nested object structures
     *  - Array structures
     *
     * @param response   REST Assured response object
     * @param schemaPath Classpath location of JSON schema
     */
    public static void validateSchema(Response response, String schemaPath) {

        if (response == null) {
            throw new IllegalArgumentException(
                    "Response object cannot be null while performing schema validation"
            );
        }

        if (schemaPath == null || schemaPath.isEmpty()) {
            throw new IllegalArgumentException(
                    "Schema path must be provided for schema validation"
            );
        }

        log.info("Validating API response against schema: {}", schemaPath);

        /**
         * REST Assured native schema validation.
         *
         * This ensures:
         * - Response structure matches schema
         * - Required properties exist
         * - Correct data types are returned
         */
        response
                .then()
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath(schemaPath));

        log.info("Schema validation successful for schema: {}", schemaPath);
    }
}