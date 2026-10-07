
package com.pratik.factory;

import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.Playwright;
import com.pratik.utils.ConfigReader;

import java.util.Map;

public class ApiFactory {

    private static final ThreadLocal<Playwright> tlPlaywright = new ThreadLocal<>();
    private static final ThreadLocal<APIRequestContext> tlRequest = new ThreadLocal<>();

    public static APIRequestContext initApi() {
        Playwright playwright = Playwright.create();
        APIRequestContext request = playwright.request().newContext(
                new APIRequest.NewContextOptions()
                        .setBaseURL(ConfigReader.get("api.url"))
                        .setExtraHTTPHeaders(Map.of("Accept", "application/json")));
        tlPlaywright.set(playwright);
        tlRequest.set(request);
        return request;
    }

    public static APIRequestContext getRequest() {
        return tlRequest.get();
    }

    public static void closeApi() {
        if (tlRequest.get() != null) tlRequest.get().dispose();
        if (tlPlaywright.get() != null) tlPlaywright.get().close();
        tlRequest.remove();
        tlPlaywright.remove();
    }
}