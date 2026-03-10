package com.company.automation.services.user.resource;

import com.company.automation.context.ScenarioContext;
import com.company.automation.core.resource.ApiResource;
import com.company.automation.services.user.model.UserRequest;
import com.company.automation.services.user.model.UserResponse;

public class UserResource extends ApiResource<UserRequest, UserResponse> {

    public UserResource(ScenarioContext scenarioContext) {

        super(
                "/users",
                UserResponse.class,
                UserResponse[].class,
                scenarioContext
        );
    }
}