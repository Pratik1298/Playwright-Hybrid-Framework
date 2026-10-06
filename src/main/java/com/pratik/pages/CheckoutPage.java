package com.pratik.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.pratik.model.Address;

public class CheckoutPage {
    private final Page page;

    // Step 1: Cart
    private final Locator cartProductTitle;
    private final Locator productPrice;
    private final Locator linePrice;
    private final Locator continueShoppingButton;
    private final Locator proceedToCheckoutButton;

    // Step 2: Sign in as guest
    private final Locator continueAsGuestTab;
    private final Locator guestEmail;
    private final Locator guestFirstName;
    private final Locator guestLastName;
    private final Locator continueAsGuestButton;
    private final Locator proceedAsGuestButton;

    // Step 3: Billing address
    private final Locator country;
    private final Locator postalCode;
    private final Locator houseNumber;
    private final Locator street;
    private final Locator city;
    private final Locator state;
    private final Locator proceedToPaymentButton;

    // Step 4: Payment
    private final Locator paymentMethod;

    public CheckoutPage(Page page) {
        this.page = page;

        cartProductTitle = page.locator("span.product-title");
        productPrice = page.getByTestId("product-price");
        linePrice = page.getByTestId("line-price");
        continueShoppingButton = page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Continue Shopping"));
        proceedToCheckoutButton = page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Proceed to checkout"));

        continueAsGuestTab = page.locator("//a[text()='Continue as Guest']");
        guestEmail = page.locator("#guest-email");
        guestFirstName = page.locator("#guest-first-name");
        guestLastName = page.locator("#guest-last-name");
        continueAsGuestButton = page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Continue as Guest"));
        proceedAsGuestButton = page.getByTestId("proceed-2-guest");

        country = page.locator("#country");
        postalCode = page.locator("#postal_code");
        houseNumber = page.locator("#house_number");
        street = page.locator("#street");
        city = page.locator("#city");
        state = page.locator("#state");
        proceedToPaymentButton = page.getByTestId("proceed-3");

        paymentMethod = page.getByTestId("payment-method");
    }

    public Locator cartProductTitle() { return cartProductTitle; }
    public Locator productPrice() { return productPrice; }
    public Locator linePrice() { return linePrice; }
    public Locator continueShoppingButton() { return continueShoppingButton; }
    public Locator proceedToPaymentButton() { return proceedToPaymentButton; }
    public Locator paymentMethod() { return paymentMethod; }

    public void proceedToCheckout() {
        proceedToCheckoutButton.click();
    }

    public void continueAsGuest(String email, String firstName, String lastName) {
        continueAsGuestTab.click();
        guestEmail.fill(email);
        guestFirstName.fill(firstName);
        guestLastName.fill(lastName);
        continueAsGuestButton.click();
    }

    public void proceedAsGuest() {
        proceedAsGuestButton.click();
    }

    public void fillBillingAddress(Address address) {
        country.selectOption(address.country());
        postalCode.fill(address.postalCode());
        houseNumber.fill(address.houseNumber());
        street.fill(address.street());
        city.fill(address.city());
        state.fill(address.state());
    }

    public void proceedToPayment() {
        proceedToPaymentButton.click();
    }
}