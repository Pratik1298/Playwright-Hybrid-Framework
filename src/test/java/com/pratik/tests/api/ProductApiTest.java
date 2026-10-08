package com.pratik.tests.api;

import com.google.gson.JsonObject;
import com.microsoft.playwright.APIResponse;
import com.pratik.api.ProductApi;
import com.pratik.api.models.Product;
import com.pratik.api.models.ProductPage;
import com.pratik.api.models.ProductRequest;
import com.pratik.base.ApiBaseTest;
import com.pratik.utils.JsonUtil;
import com.pratik.utils.TestDataFactory;
import org.testng.annotations.Test;

import java.util.Map;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.testng.Assert.*;

public class ProductApiTest extends ApiBaseTest {

    @Test(description = "GET /products returns a non-empty list with valid prices")
    public void getProductsReturnsList() {
        APIResponse response = new ProductApi(request()).getProducts();

        assertThat(response).isOK();
        ProductPage page = JsonUtil.fromJson(response.text(), ProductPage.class);
        assertFalse(page.data().isEmpty(), "Product list should not be empty");
        assertTrue(page.data().stream().allMatch(p -> p.price() > 0),
                "Every product should have a positive price");
    }

    @Test(description = "GET /products/{id} returns the same product as the list")
    public void getSingleProductById() {
        ProductApi productApi = new ProductApi(request());
        ProductPage page = JsonUtil.fromJson(productApi.getProducts().text(), ProductPage.class);
        Product first = page.data().get(0);

        APIResponse response = productApi.getProduct(first.id());

        assertThat(response).isOK();
        Product product = JsonUtil.fromJson(response.text(), Product.class);
        assertEquals(product.id(), first.id());
        assertEquals(product.name(), first.name());
        assertNotNull(product.brand(), "Product should have a brand");
    }
    private Product referenceProduct(ProductApi productApi) {
        ProductPage page = JsonUtil.fromJson(productApi.getProducts().text(), ProductPage.class);
        String id = page.data().get(0).id();
        return JsonUtil.fromJson(productApi.getProduct(id).text(), Product.class);
    }

    @Test(description = "POST /products creates a product using IDs from existing data")
    public void createProduct() {
        ProductApi productApi = new ProductApi(request());
        Product reference = referenceProduct(productApi);
        String name = "PW Wire Cutter " + TestDataFactory.uniqueSuffix();

        APIResponse response = productApi.createProduct(ProductRequest.basedOn(reference, name, 12.99));

        assertEquals(response.status(), 201, "Product should be created: " + response.text());
        Product created = JsonUtil.fromJson(response.text(), Product.class);

        Product fetched = JsonUtil.fromJson(productApi.getProduct(created.id()).text(), Product.class);
        assertEquals(fetched.name(), name);
        assertEquals(fetched.price(), 12.99, 0.001);
        assertEquals(fetched.brand().id(), reference.brand().id());
        assertEquals(fetched.category().id(), reference.category().id());
    }

    @Test(description = "PATCH /products/{id} changes only the price")
    public void patchProductPrice() {
        ProductApi productApi = new ProductApi(request());
        Product reference = referenceProduct(productApi);
        String name = "PW Patch Target " + TestDataFactory.uniqueSuffix();
        APIResponse createResponse = productApi.createProduct(ProductRequest.basedOn(reference, name, 10.00));
        Product created = JsonUtil.fromJson(createResponse.text(), Product.class);

        APIResponse response = productApi.patchProduct(created.id(), Map.of("price", 19.99));

        assertThat(response).isOK();
        Product fetched = JsonUtil.fromJson(productApi.getProduct(created.id()).text(), Product.class);
        assertEquals(fetched.price(), 19.99, 0.001);
        assertEquals(fetched.name(), name, "Name should not change");
    }

    @Test(description = "POST /products without required fields is rejected with 422")
    public void createProductWithMissingFields() {
        APIResponse response = new ProductApi(request()).createProduct(
                new ProductRequest("Incomplete", null, 5.00, null, null, null, false, false, null));

        assertEquals(response.status(), 422);
        JsonObject errors = JsonUtil.toJsonObject(response.text());
        assertTrue(errors.has("category_id"), "Error should mention category_id");
        assertTrue(errors.has("brand_id"), "Error should mention brand_id");
    }
}
