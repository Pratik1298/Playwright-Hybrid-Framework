package com.pratik.api;

import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.pratik.api.models.LoginRequest;
import com.pratik.api.models.LoginResponse;
import com.pratik.utils.JsonUtil;

public class UserApi extends BaseApi {

    private static final String BASE_PATH = "/users";

    public UserApi(APIRequestContext request) {
        super(request);
    }

    public APIResponse login(LoginRequest credentials) {
        return post(BASE_PATH + "/login", credentials);
    }

    public String loginAndGetToken(String email, String password) {
        APIResponse response = login(new LoginRequest(email, password));
        if (!response.ok()) {
            throw new IllegalStateException("Login failed with status " + response.status());
        }
        return JsonUtil.fromJson(response.text(), LoginResponse.class).accessToken();
    }

    public APIResponse getProfile() {
        return get(BASE_PATH + "/me");
    }
}