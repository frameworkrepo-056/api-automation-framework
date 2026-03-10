package com.company.automation.runners;

import com.company.automation.utils.AllureEnvironmentWriter;
import org.junit.platform.suite.api.*;

import static io.cucumber.junit.platform.engine.Constants.*;

/**
 * Runs only @severity:critical scenarios — ideal for smoke/sanity pipelines.
 * Usage: mvn test -Dtest=SmokeTestRunner
 */
@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME,   value = "com.company.automation")
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME,  value = "pretty, io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm")
@ConfigurationParameter(key = FILTER_TAGS_PROPERTY_NAME, value = "@severity:critical and not @ignore")
public class SmokeTestRunner {

    static {
        AllureEnvironmentWriter.writeEnvironmentInfo();
    }
}