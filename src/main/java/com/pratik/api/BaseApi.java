package com.pratik.api;

import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.options.RequestOptions;
import com.pratik.utils.JsonUtil;

public abstract class BaseApi {

    protected final APIRequestContext request;
    private String token;

    protected BaseApi(APIRequestContext request) {
        this.request = request;
    }

    public void setToken(String token) {
        this.token = token;
    }

    private RequestOptions options() {
        RequestOptions options = RequestOptions.create();
        if (token != null) {
            options.setHeader("Authorization", "Bearer " + token);
        }
        return options;
    }

    private RequestOptions jsonOptions(Object body) {
        return options()
                .setHeader("Content-Type", "application/json")
                .setData(JsonUtil.toJson(body));
    }

    protected APIResponse get(String path) {
        return request.get(path, options());
    }

    protected APIResponse post(String path, Object body) {
        return request.post(path, jsonOptions(body));
    }

    protected APIResponse put(String path, Object body) {
        return request.put(path, jsonOptions(body));
    }

    protected APIResponse patch(String path, Object body) {
        return request.patch(path, jsonOptions(body));
    }

    protected APIResponse delete(String path) {
        return request.delete(path, options());
    }
}