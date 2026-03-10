@severity:critical
Feature: Create User API

  @SmokeTestResource
  Scenario: Create user using resource layer
    When I create a user using resource layer
    Then the response status should be 201
    And the response body should not be empty