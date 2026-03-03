@epic:User-Service
@feature:Get-Users
Feature: Get Users API Validation

  @story:Fetch-All-Users
  @severity:critical
  @layer:api
  Scenario: Verify users list API returns 200 and correct count
    Given I call get users API
    Then the response status should be 200
    And the response should contain 10 users

  @story:Validate-User-Schema
  @severity:normal
  @layer:api
  Scenario: Verify users list API schema validation
    Given I call get users API
    Then the response status should be 200
    And the response should match "get_users_schema.json" schema