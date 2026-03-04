package com.company.automation.services.user.endpoints;

/**
 * All endpoint paths for the User service.
 * Use these constants everywhere — never hardcode endpoint strings.
 */
public class UserEndpoints {

    private UserEndpoints() { /* Utility class — no instantiation */ }

    public static final String GET_USERS       = "/users";
    public static final String GET_USER_BY_ID  = "/users/{id}";   // NEW
    public static final String CREATE_USER     = "/users";         // NEW
    public static final String UPDATE_USER     = "/users/{id}";   // NEW
    public static final String DELETE_USER     = "/users/{id}";   // NEW
}