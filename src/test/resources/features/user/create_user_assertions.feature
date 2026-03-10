Feature: Create user using resource pattern with assertions

  @api @user @post
  Scenario: Create a new user and validate response

    When I create a random user using resource
    Then the API response should be valid for user creation