package com.pratik.api;

import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.options.RequestOptions;
import com.pratik.utils.JsonUtil;
import io.qameta.allure.Allure;

public abstract class BaseApi {

    protected final APIRequestContext request;
    private String token;

    protected BaseApi(APIRequestContext request) {
        this.request = request;
    }

    public void setToken(String token) {
        this.token = token;
    }

    protected APIResponse get(String path) {
        return send("GET", path, null);
    }

    protected APIResponse post(String path, Object body) {
        return send("POST", path, body);
    }

    protected APIResponse put(String path, Object body) {
        return send("PUT", path, body);
    }

    protected APIResponse patch(String path, Object body) {
        return send("PATCH", path, body);
    }

    protected APIResponse delete(String path) {
        return send("DELETE", path, null);
    }

    private APIResponse send(String method, String path, Object body) {
        RequestOptions options = RequestOptions.create().setMethod(method);
        if (token != null) {
            options.setHeader("Authorization", "Bearer " + token);
        }
        String json = body == null ? null : JsonUtil.toJson(body);
        if (json != null) {
            options.setHeader("Content-Type", "application/json").setData(json);
        }

        return Allure.step(method + " " + path, () -> {
            if (json != null) {
                Allure.addAttachment("Request body", "application/json", json, ".json");
            }
            APIResponse response = request.fetch(path, options);
            Allure.addAttachment("Response " + response.status(), "application/json",
                    response.text(), ".json");
            return response;
        });
    }
}