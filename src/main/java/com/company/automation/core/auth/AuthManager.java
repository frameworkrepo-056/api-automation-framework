package com.company.automation.core.auth;

import com.company.automation.core.config.ConfigReader;
import com.company.automation.core.reporting.AllureAttachmentUtil;
import io.restassured.specification.RequestSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Central authentication manager.
 * Applies authentication strategy dynamically based on configuration.
 */
public class AuthManager {

    private static final Logger log = LoggerFactory.getLogger(AuthManager.class);

    public static void applyAuth(RequestSpecification spec) {

        String authType = ConfigReader.get("auth.type");
        String token = ConfigReader.get("auth.token");

        log.info("Auth type from config: {}", authType);
        log.info("Auth token from config: {}", token);

        AllureAttachmentUtil.attachAuthToken(ConfigReader.get("auth.token"));

        if (authType == null || authType.isBlank()) {
            log.info("No authentication configured.");
            return;
        }

        switch (authType.toLowerCase()) {

            case "bearer":

                if (token == null || token.isBlank()) {
                    throw new IllegalStateException("Bearer token is missing in config.");
                }

                new BearerTokenAuth(token).apply(spec);

                log.info("Bearer token authentication applied.");
                break;

            case "apikey":

                String header = ConfigReader.get("auth.apikey.header");
                String key = ConfigReader.get("auth.apikey.value");

                new ApiKeyAuth(header, key).apply(spec);

                log.info("API key authentication applied. Header: {}", header);
                break;

            case "oauth2":

                if (token == null || token.isBlank()) {
                    throw new IllegalStateException("OAuth token missing in config.");
                }

                new OAuth2Auth(token).apply(spec);

                log.info("OAuth2 authentication applied.");
                break;

            default:
                throw new IllegalArgumentException(
                        "Unsupported auth type: " + authType
                );
        }
    }
}