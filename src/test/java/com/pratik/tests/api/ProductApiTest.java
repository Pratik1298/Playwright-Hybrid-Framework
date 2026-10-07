package com.pratik.tests.api;

import com.microsoft.playwright.APIResponse;
import com.pratik.api.ProductApi;
import com.pratik.api.models.Product;
import com.pratik.api.models.ProductPage;
import com.pratik.base.ApiBaseTest;
import com.pratik.utils.JsonUtil;
import org.testng.annotations.Test;

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
}
