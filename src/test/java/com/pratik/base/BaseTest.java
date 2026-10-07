package com.pratik.base;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.Tracing;
import com.pratik.factory.PlaywrightFactory;
import com.pratik.utils.ConfigReader;
import io.qameta.allure.Allure;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.io.ByteArrayInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class BaseTest {

    @BeforeMethod
    public void setUp() {
        PlaywrightFactory.initBrowser();
        page().navigate(ConfigReader.get("url"));
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        try {
            if (result.getStatus() == ITestResult.FAILURE) {
                attachFailureArtifacts(result.getMethod().getMethodName());
            }
        } finally {
            PlaywrightFactory.closeBrowser();
        }
    }

    private void attachFailureArtifacts(String testName) {
        try {
            byte[] screenshot = page().screenshot(
                    new Page.ScreenshotOptions().setFullPage(true));
            Allure.addAttachment("Screenshot - " + testName, "image/png",
                    new ByteArrayInputStream(screenshot), ".png");

            Path tracePath = Paths.get("test-results", "traces",
                    testName + "_" + System.currentTimeMillis() + ".zip");
            Files.createDirectories(tracePath.getParent());
            PlaywrightFactory.getContext().tracing()
                    .stop(new Tracing.StopOptions().setPath(tracePath));
            Allure.addAttachment("Playwright trace - " + testName, "application/zip",
                    new ByteArrayInputStream(Files.readAllBytes(tracePath)), ".zip");

            System.out.println("Trace saved: " + tracePath.toAbsolutePath());
        } catch (Exception e) {
            System.err.println("Could not save failure artifacts: " + e.getMessage());
        }
    }

    protected Page page() {
        return PlaywrightFactory.getPage();
    }
}
