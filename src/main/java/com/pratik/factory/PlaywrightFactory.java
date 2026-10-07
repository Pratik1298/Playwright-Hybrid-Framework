package com.pratik.factory;

import com.microsoft.playwright.*;
import com.pratik.utils.ConfigReader;

public class PlaywrightFactory {

    private static final ThreadLocal<Playwright> tlPlaywright = new ThreadLocal<>();
    private static final ThreadLocal<Browser> tlBrowser = new ThreadLocal<>();
    private static final ThreadLocal<BrowserContext> tlContext = new ThreadLocal<>();
    private static final ThreadLocal<Page> tlPage = new ThreadLocal<>();

    public static Page initBrowser() {
        String browserName = ConfigReader.get("browser").trim().toLowerCase();
        boolean headless = Boolean.parseBoolean(ConfigReader.get("headless"));

        Playwright playwright = Playwright.create();
        playwright.selectors().setTestIdAttribute("data-test");
        BrowserType.LaunchOptions options = new BrowserType.LaunchOptions().setHeadless(headless);

        Browser browser = switch (browserName) {
            case "chromium" -> playwright.chromium().launch(options);
            case "firefox"  -> playwright.firefox().launch(options);
            case "webkit"   -> playwright.webkit().launch(options);
            case "chrome"   -> playwright.chromium().launch(options.setChannel("chrome"));
            case "edge"     -> playwright.chromium().launch(options.setChannel("msedge"));
            default -> throw new IllegalArgumentException("Unsupported browser: " + browserName);
        };

        BrowserContext context = browser.newContext(
                new Browser.NewContextOptions().setViewportSize(1920, 1080));
        context.tracing().start(new Tracing.StartOptions()
                .setScreenshots(true)
                .setSnapshots(true));
        Page page = context.newPage();

        tlPlaywright.set(playwright);
        tlBrowser.set(browser);
        tlContext.set(context);
        tlPage.set(page);
        return page;
    }

    public static Page getPage() {
        return tlPage.get();
    }

    public static BrowserContext getContext() {
        return tlContext.get();
    }

    public static void closeBrowser() {
        if (tlContext.get() != null) tlContext.get().close();
        if (tlBrowser.get() != null) tlBrowser.get().close();
        if (tlPlaywright.get() != null) tlPlaywright.get().close();
        tlPage.remove();
        tlContext.remove();
        tlBrowser.remove();
        tlPlaywright.remove();
    }
}
