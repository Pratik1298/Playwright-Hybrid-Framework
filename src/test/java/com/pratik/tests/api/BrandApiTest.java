package com.pratik.tests.api;

import com.google.gson.JsonObject;
import com.microsoft.playwright.APIResponse;
import com.pratik.api.BrandApi;
import com.pratik.api.models.Brand;
import com.pratik.api.models.BrandRequest;
import com.pratik.api.models.SuccessResponse;
import com.pratik.base.ApiBaseTest;
import com.pratik.utils.JsonUtil;
import com.pratik.utils.TestDataFactory;
import org.testng.annotations.Test;

import java.util.Map;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.testng.Assert.*;

public class BrandApiTest extends ApiBaseTest {

    private Brand createUniqueBrand(BrandApi brandApi) {
        BrandRequest request = new BrandRequest("PW Brand", TestDataFactory.uniqueSlug("pw-brand"));
        APIResponse response = brandApi.createBrand(request);
        assertEquals(response.status(), 201, "Brand should be created: " + response.text());
        return JsonUtil.fromJson(response.text(), Brand.class);
    }

    @Test(description = "GET /brands returns a non-empty list")
    public void getAllBrands() {
        APIResponse response = new BrandApi(request()).getBrands();

        assertThat(response).isOK();
        Brand[] brands = JsonUtil.fromJson(response.text(), Brand[].class);
        assertTrue(brands.length > 0, "There should be at least one brand");
    }

    @Test(description = "POST /brands creates a brand that can then be fetched by ID")
    public void createBrand() {
        BrandApi brandApi = new BrandApi(request());
        String slug = TestDataFactory.uniqueSlug("pw-brand");

        APIResponse response = brandApi.createBrand(new BrandRequest("PW Brand", slug));

        assertEquals(response.status(), 201);
        Brand created = JsonUtil.fromJson(response.text(), Brand.class);
        assertNotNull(created.id(), "New brand should have an ID");
        assertEquals(created.slug(), slug);

        Brand fetched = JsonUtil.fromJson(brandApi.getBrand(created.id()).text(), Brand.class);
        assertEquals(fetched.name(), "PW Brand");
    }

    @Test(description = "PUT /brands/{id} replaces name and slug")
    public void updateBrand() {
        BrandApi brandApi = new BrandApi(request());
        Brand brand = createUniqueBrand(brandApi);
        String newSlug = TestDataFactory.uniqueSlug("pw-updated");

        APIResponse response = brandApi.updateBrand(brand.id(), new BrandRequest("PW Updated", newSlug));

        assertThat(response).isOK();
        assertTrue(JsonUtil.fromJson(response.text(), SuccessResponse.class).success());
        Brand fetched = JsonUtil.fromJson(brandApi.getBrand(brand.id()).text(), Brand.class);
        assertEquals(fetched.name(), "PW Updated");
        assertEquals(fetched.slug(), newSlug);
    }

    @Test(description = "PATCH /brands/{id} changes only the name")
    public void patchBrandName() {
        BrandApi brandApi = new BrandApi(request());
        Brand brand = createUniqueBrand(brandApi);

        APIResponse response = brandApi.patchBrand(brand.id(), Map.of("name", "PW Patched"));

        assertThat(response).isOK();
        Brand fetched = JsonUtil.fromJson(brandApi.getBrand(brand.id()).text(), Brand.class);
        assertEquals(fetched.name(), "PW Patched");
        assertEquals(fetched.slug(), brand.slug(), "Slug should not change");
    }

    @Test(description = "POST /brands with a duplicate slug is rejected")
    public void createBrandWithDuplicateSlug() {
        BrandApi brandApi = new BrandApi(request());
        Brand existing = createUniqueBrand(brandApi);

        APIResponse response = brandApi.createBrand(new BrandRequest("Duplicate", existing.slug()));

        assertTrue(response.status() == 409 || response.status() == 422,
                "Expected 409 or 422 but got " + response.status());
        assertTrue(JsonUtil.toJsonObject(response.text()).has("slug"),
                "Error should mention the slug field");
    }

    @Test(description = "POST /brands without a name is rejected with 422")
    public void createBrandWithoutName() {
        BrandApi brandApi = new BrandApi(request());

        APIResponse response = brandApi.createBrand(
                new BrandRequest(null, TestDataFactory.uniqueSlug("pw-noname")));

        assertEquals(response.status(), 422);
        JsonObject errors = JsonUtil.toJsonObject(response.text());
        assertTrue(errors.has("name"), "Error should mention the name field");
    }
}
