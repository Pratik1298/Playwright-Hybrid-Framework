package com.pratik.api;

import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;

public class ProductApi extends BaseApi {

    private static final String BASE_PATH = "/products";

    public ProductApi(APIRequestContext request) {
        super(request);
    }

    public APIResponse getProducts() {
        return get(BASE_PATH);
    }

    public APIResponse getProduct(String id) {
        return get(BASE_PATH + "/" + id);
    }
}
