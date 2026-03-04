package com.company.automation.core.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Properties;

/**
 * Loads configuration from config/config.properties on the classpath.
 * Supports runtime override via system properties:
 *   mvn test -Denv=staging
 *   mvn test -Dqa.base.url=https://my-api.com
 *
 * Priority: System property > config.properties value
 */
public class ConfigReader {

    private static final Logger log = LoggerFactory.getLogger(ConfigReader.class);
    private static final Properties properties = new Properties();

    static {
        String configFile = "config/config.properties";
        try (InputStream input =
                     ConfigReader.class.getClassLoader().getResourceAsStream(configFile)) {
            if (input == null) {
                throw new RuntimeException("Config file not found on classpath: " + configFile);
            }
            properties.load(input);
            log.info("Configuration loaded from: {}", configFile);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load config file: " + configFile, e);
        }
    }

    /**
     * Returns value for key, with system property taking precedence.
     * Allows CI override: mvn test -Dqa.base.url=https://staging-api.com
     */
    public static String get(String key) {
        String sysValue = System.getProperty(key);
        if (sysValue != null && !sysValue.isBlank()) {
            log.debug("Using system property override for '{}': {}", key, sysValue);
            return sysValue;
        }
        return properties.getProperty(key);
    }

    /**
     * Resolves the active base URL based on env= property.
     * e.g. env=qa → reads qa.base.url
     */
    public static String getBaseUrl() {
        String env = get("env");
        if (env == null || env.isBlank()) {
            throw new RuntimeException("'env' property is not set in config.properties");
        }
        String urlKey = env + ".base.url";
        String url = get(urlKey);
        if (url == null || url.isBlank()) {
            throw new RuntimeException("No base URL configured for env '" + env + "'. Expected key: " + urlKey);
        }
        log.info("Active environment: {} → Base URL: {}", env, url);
        return url;
    }

    public static String getEnv() {
        return get("env");
    }
}