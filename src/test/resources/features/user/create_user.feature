@severity:critical
Feature: Create User API

  Scenario: Successfully create a user

    Given I create a random user
    Then the response status should be 201
    And the response body should not be empty