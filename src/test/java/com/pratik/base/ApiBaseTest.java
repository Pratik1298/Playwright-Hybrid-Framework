package com.pratik.base;

import com.microsoft.playwright.APIRequestContext;
import com.pratik.factory.ApiFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class ApiBaseTest {

    @BeforeMethod
    public void setUpApi() {
        ApiFactory.initApi();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDownApi() {
        ApiFactory.closeApi();
    }

    protected APIRequestContext request() {
        return ApiFactory.getRequest();
    }
}
