package com.pratik.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class HomePage {
    private final Page page;
    private final Locator logo;
    private final Locator searchBox;
    private final Locator searchButton;
    private final Locator productCards;

    public HomePage(Page page) {
        this.page = page;
        this.logo = page.locator("a.navbar-brand");
        this.searchBox = page.getByTestId("search-query");
        this.searchButton = page.getByTestId("search-submit");
        this.productCards = page.locator("a.card");
    }

    public Locator logo() { return logo; }
    public Locator searchBox() { return searchBox; }
    public Locator searchButton() { return searchButton; }

    public void searchFor(String productName) {
        searchBox.fill(productName);
        searchButton.click();
    }

    public Locator productCard(String productName) {
        return productCards
                .filter(new Locator.FilterOptions().setHasText(productName))
                .first();
    }

    public ProductPage openProduct(String productName) {
        productCard(productName).click();
        return new ProductPage(page);
    }
}