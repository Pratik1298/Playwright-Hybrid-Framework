package com.pratik.api;

import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.pratik.api.models.ProductRequest;

import java.util.Map;

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
    public APIResponse createProduct(ProductRequest product) {
        return post(BASE_PATH, product);
    }

    public APIResponse patchProduct(String id, Map<String, Object> fields) {
        return patch(BASE_PATH + "/" + id, fields);
    }
}
