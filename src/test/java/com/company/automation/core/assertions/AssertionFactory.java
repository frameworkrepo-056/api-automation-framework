package com.company.automation.core.assertions;

import com.company.automation.context.ScenarioContext;

public class AssertionFactory {

    public static ApiAssertions apiAssert(ScenarioContext scenarioContext) {
        return new ApiAssertions(scenarioContext);
    }
}