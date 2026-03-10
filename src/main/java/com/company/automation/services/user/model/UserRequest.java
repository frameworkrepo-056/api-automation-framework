package com.company.automation.services.user.model;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Request model for creating/updating users.
 * Used by UserDataFactory and UserClient.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserRequest {

    private String name;
    private String username;
    private String email;

    public UserRequest() {}

    public UserRequest(String name, String username, String email) {
        this.name = name;
        this.username = username;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}