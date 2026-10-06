package com.pratik.learning;

import com.microsoft.playwright.Page;
import com.pratik.factory.PlaywrightFactory;
import com.pratik.utils.ConfigReader;

public class FactoryCheck {
    public static void main(String[] args) {
        Page page = PlaywrightFactory.initBrowser();
        page.navigate(ConfigReader.get("url"));
        System.out.println("Page title: " + page.title());
        PlaywrightFactory.closeBrowser();
    }
}