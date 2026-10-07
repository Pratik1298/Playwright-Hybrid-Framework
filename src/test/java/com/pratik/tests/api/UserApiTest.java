package com.pratik.tests.api;

import com.microsoft.playwright.APIResponse;
import com.pratik.api.UserApi;
import com.pratik.api.models.LoginRequest;
import com.pratik.api.models.LoginResponse;
import com.pratik.api.models.UserProfile;
import com.pratik.base.ApiBaseTest;
import com.pratik.utils.JsonUtil;
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
}
