package com.pratik.api;

import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.pratik.api.models.BrandRequest;

import java.util.Map;

public class BrandApi extends BaseApi {

    private static final String BASE_PATH = "/brands";

    public BrandApi(APIRequestContext request) {
        super(request);
    }

    public APIResponse getBrands() {
        return get(BASE_PATH);
    }

    public APIResponse getBrand(String id) {
        return get(BASE_PATH + "/" + id);
    }

    public APIResponse createBrand(BrandRequest brand) {
        return post(BASE_PATH, brand);
    }

    public APIResponse updateBrand(String id, BrandRequest brand) {
        return put(BASE_PATH + "/" + id, brand);
    }

    public APIResponse patchBrand(String id, Map<String, Object> fields) {
        return patch(BASE_PATH + "/" + id, fields);
    }
}
