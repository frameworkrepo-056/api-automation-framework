package com.company.automation.utils;

import com.company.automation.core.config.ConfigReader;
import io.qameta.allure.Allure;

import java.util.HashMap;
import java.util.Map;

/**
 * Writes environment information into Allure report.
 * This appears under the "Environment" section in Allure.
 */
public class AllureEnvironmentWriter {

    public static void writeEnvironmentInfo() {

        Map<String, String> env = new HashMap<>();

        env.put("Environment", ConfigReader.getEnvironment());
        env.put("Base URL", ConfigReader.getBaseUrl());
        env.put("Framework", "API Automation Framework");
        env.put("Runner", "Cucumber + JUnit5");

        env.forEach((k, v) -> Allure.label(k, v));
    }
}