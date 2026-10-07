package com.pratik.tests.api;

import com.microsoft.playwright.APIResponse;
import com.pratik.api.UserApi;
import com.pratik.api.models.LoginRequest;
import com.pratik.api.models.LoginResponse;
import com.pratik.api.models.RegisterRequest;
import com.pratik.api.models.UserProfile;
import com.pratik.base.ApiBaseTest;
import com.pratik.utils.JsonUtil;
import com.pratik.utils.TestDataFactory;
import org.testng.annotations.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.testng.Assert.*;

public class UserApiTest extends ApiBaseTest {

    private static final String EMAIL = "customer@practicesoftwaretesting.com";
    private static final String PASSWORD = "welcome01";

    @Test(description = "Login with valid credentials returns a bearer token")
    public void loginWithValidCredentials() {
        APIResponse response = new UserApi(request()).login(new LoginRequest(EMAIL, PASSWORD));

        assertThat(response).isOK();
        LoginResponse login = JsonUtil.fromJson(response.text(), LoginResponse.class);
        assertEquals(login.tokenType(), "bearer");
        assertFalse(login.accessToken().isBlank(), "Access token should not be blank");
        assertTrue(login.expiresIn() > 0, "Token should have an expiry time");
    }

    @Test(description = "Login with a wrong password is rejected with 401")
    public void loginWithWrongPassword() {
        APIResponse response = new UserApi(request()).login(new LoginRequest(EMAIL, "wrong-password"));

        assertEquals(response.status(), 401);
    }

    @Test(description = "GET /users/me returns the logged-in user's profile")
    public void getProfileWithToken() {
        UserApi userApi = new UserApi(request());
        userApi.setToken(userApi.loginAndGetToken(EMAIL, PASSWORD));

        APIResponse response = userApi.getProfile();

        assertThat(response).isOK();
        UserProfile profile = JsonUtil.fromJson(response.text(), UserProfile.class);
        assertEquals(profile.email(), EMAIL);
        assertEquals(profile.firstName(), "Jane");
    }

    @Test(description = "GET /users/me without a token is rejected with 401")
    public void getProfileWithoutToken() {
        APIResponse response = new UserApi(request()).getProfile();

        assertEquals(response.status(), 401);
    }
    @Test(description = "A newly registered user can log in and read their profile")
    public void registerNewUserAndLogin() {
        UserApi userApi = new UserApi(request());
        String email = TestDataFactory.uniqueEmail();
        String password = TestDataFactory.strongPassword();

        APIResponse registerResponse = userApi.register(new RegisterRequest(
                "Pratik", "Tester", "1998-05-12", "0871234567", email, password));
        assertEquals(registerResponse.status(), 201, "Registration failed: " + registerResponse.text());

        userApi.setToken(userApi.loginAndGetToken(email, password));
        UserProfile profile = JsonUtil.fromJson(userApi.getProfile().text(), UserProfile.class);
        assertEquals(profile.email(), email);
        assertEquals(profile.firstName(), "Pratik");
    }

    @Test(description = "Registering with an existing email is rejected")
    public void registerWithExistingEmail() {
        APIResponse response = new UserApi(request()).register(new RegisterRequest(
                "Jane", "Doe", "1990-01-01", "0871234567",
                EMAIL, TestDataFactory.strongPassword()));

        assertTrue(response.status() == 409 || response.status() == 422,
                "Expected 409 or 422 but got " + response.status());
        assertTrue(JsonUtil.toJsonObject(response.text()).has("email"));
    }
}
