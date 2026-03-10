package com.company.automation.testdata.user;

import com.company.automation.services.user.model.UserRequest;
import net.datafaker.Faker;

/**
 * Factory class for generating user test data.
 * Uses Datafaker for realistic random data.
 */
public class UserDataFactory {

    private static final Faker faker = new Faker();

    private UserDataFactory() {}

    /**
     * Generate a random user payload.
     */
    public static UserRequest createRandomUser() {

        UserRequest user = new UserRequest();

        user.setName(faker.name().fullName());
        user.setUsername(faker.name().username());
        user.setEmail(faker.internet().emailAddress());

        return user;
    }

    /**
     * Generate deterministic user for predictable tests.
     */
    public static UserRequest createDefaultUser() {

        return new UserRequest(
                "Test User",
                "testuser",
                "testuser@example.com"
        );
    }

    /**
     * Generate update payload.
     */
    public static UserRequest updateUserPayload() {

        UserRequest user = new UserRequest();

        user.setName(faker.name().fullName());
        user.setEmail(faker.internet().emailAddress());

        return user;
    }
}