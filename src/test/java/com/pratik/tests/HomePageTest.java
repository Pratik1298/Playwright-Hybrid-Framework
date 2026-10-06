package com.pratik.tests;

import com.pratik.base.BaseTest;
import com.pratik.pages.HomePage;
import com.pratik.utils.ConfigReader;
import org.testng.annotations.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class HomePageTest extends BaseTest {

    @Test
    public void verifyHomePageUrl() {
        assertThat(page()).hasURL(ConfigReader.get("url"));
    }

    @Test
    public void verifyLogoIsVisible() {
        HomePage home = new HomePage(page());
        assertThat(home.logo()).isVisible();
    }

    @Test
    public void searchForProduct() {
        HomePage home = new HomePage(page());
        assertThat(home.searchBox()).isVisible();
        home.searchFor("Combination Pliers");
        assertThat(home.productCard("Combination Pliers")).isVisible();
    }
}