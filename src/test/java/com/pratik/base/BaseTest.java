package com.pratik.base;

import com.microsoft.playwright.Page;
import com.pratik.factory.PlaywrightFactory;
import com.pratik.utils.ConfigReader;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class BaseTest {

    @BeforeMethod
    public void setUp() {
        PlaywrightFactory.initBrowser();
        page().navigate(ConfigReader.get("url"));
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        PlaywrightFactory.closeBrowser();
    }

    protected Page page() {
        return PlaywrightFactory.getPage();
    }
}
