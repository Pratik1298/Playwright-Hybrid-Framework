package com.pratik.tests;

import com.pratik.base.BaseTest;
import com.pratik.model.Address;
import com.pratik.pages.CheckoutPage;
import com.pratik.pages.HomePage;
import com.pratik.pages.ProductPage;
import org.testng.annotations.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class CheckoutTest extends BaseTest {

    private static final String PRODUCT = "Combination Pliers";

    private CheckoutPage addProductAndOpenCart() {
        HomePage home = new HomePage(page());
        home.searchFor(PRODUCT);
        ProductPage product = home.openProduct(PRODUCT);
        product.addToCart();
        assertThat(product.cartCount()).hasText("1");
        return product.openCart();
    }

    @Test
    public void verifyCartContents() {
        CheckoutPage checkout = addProductAndOpenCart();
        assertThat(checkout.cartProductTitle()).containsText(PRODUCT);
        assertThat(checkout.productPrice()).isVisible();
        assertThat(checkout.linePrice()).isVisible();
        assertThat(checkout.continueShoppingButton()).isVisible();
    }

    @Test
    public void guestCheckoutReachesPaymentStep() {
        CheckoutPage checkout = addProductAndOpenCart();
        checkout.proceedToCheckout();
        checkout.continueAsGuest("pratik.test@example.com", "Pratik", "Bhosale");
        checkout.proceedAsGuest();

        Address address = new Address("Ireland", "N37 F9H0", "123",
                "Dublin Road", "Athlone", "Westmeath");
        checkout.fillBillingAddress(address);

        assertThat(checkout.proceedToPaymentButton()).isEnabled();
        checkout.proceedToPayment();
        assertThat(checkout.paymentMethod()).isVisible();
    }
}
