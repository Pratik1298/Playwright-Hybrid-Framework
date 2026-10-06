package com.pratik.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class ProductPage {
    private final Page page;
    private final Locator productTitle;
    private final Locator priceSection;
    private final Locator description;
    private final Locator productImage;
    private final Locator addToCartButton;
    private final Locator cartIcon;
    private final Locator cartCount;

    public ProductPage(Page page) {
        this.page = page;
        this.productTitle = page.getByTestId("product-name");
        this.priceSection = page.locator(".price-section");
        this.description = page.locator("#description");
        this.productImage = page.locator("img.figure-img");
        this.addToCartButton = page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Add to cart"));
        this.cartIcon = page.getByRole(AriaRole.LINK,
                new Page.GetByRoleOptions().setName("cart").setExact(true));
        this.cartCount = page.locator("#lblCartCount");
    }

    public Locator productTitle() { return productTitle; }
    public Locator priceSection() { return priceSection; }
    public Locator description() { return description; }
    public Locator productImage() { return productImage; }
    public Locator addToCartButton() { return addToCartButton; }
    public Locator cartCount() { return cartCount; }

    public void addToCart() {
        addToCartButton.click();
    }

    public CheckoutPage openCart() {
        cartIcon.click();
        return new CheckoutPage(page);
    }
}
