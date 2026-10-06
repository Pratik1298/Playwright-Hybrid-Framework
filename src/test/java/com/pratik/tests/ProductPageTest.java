package com.pratik.tests;

import com.pratik.base.BaseTest;
import com.pratik.pages.HomePage;
import com.pratik.pages.ProductPage;
import org.testng.annotations.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class ProductPageTest extends BaseTest {

    private static final String PRODUCT = "Combination Pliers";

    private ProductPage openProductPage() {
        HomePage home = new HomePage(page());
        home.searchFor(PRODUCT);
        return home.openProduct(PRODUCT);
    }

    @Test
    public void verifyProductDetails() {
        ProductPage product = openProductPage();
        assertThat(product.productTitle()).containsText(PRODUCT);
        assertThat(product.priceSection()).isVisible();
        assertThat(product.description()).isVisible();
        assertThat(product.productImage()).isVisible();
        assertThat(product.addToCartButton()).isEnabled();
    }

    @Test
    public void addProductToCart() {
        ProductPage product = openProductPage();
        product.addToCart();
        assertThat(product.cartCount()).hasText("1");
    }
}
