@epic:User-Service
@feature:Get-Users
@layer:api
Feature: Get Users API Validation

  # FIX: Background eliminates repeated "Given I call get users API" across scenarios
  Background:
    Given I call get users API

  @story:Fetch-All-Users
  @severity:critical
  Scenario: Verify users list API returns 200 with users
    Then the response status should be 200
    And the response body should not be empty
    And the response should contain 10 users
    And the response time should be less than 3000 ms

  @story:Validate-User-Schema
  @severity:normal
  Scenario: Verify users list API schema validation
    Then the response status should be 200
    And the response should match "get_users_schema.json" schema

  @story:Response-Time-SLA
  @severity:normal
  Scenario: Verify users list API meets response time SLA
    Then the response time should be less than 3000 ms